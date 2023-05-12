package com.example.ordermicroservice.service;

import com.example.ordermicroservice.entities.Orders;
import com.example.ordermicroservice.exceptions.CustomFeignErrorResponseException;
import com.example.ordermicroservice.exceptions.OrderNotFoundException;
import com.example.ordermicroservice.external.client.PaymentService;
import com.example.ordermicroservice.external.client.ProductService;
import com.example.ordermicroservice.external.request.PaymentRequest;
import com.example.ordermicroservice.external.response.PaymentResponse;
import com.example.ordermicroservice.model.OrderModel;
import com.example.ordermicroservice.model.OrderResponse;
import com.example.ordermicroservice.external.response.ProductResponse;
import com.example.ordermicroservice.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;

    private final ProductService productService;

    private final PaymentService paymentService;

    private final RestTemplate restTemplate;

    @Override
    public OrderModel saveOrder(OrderModel orderModel) {

        // Product Service -> Block Products (Reduce the quantity after orderModel)
        productService.reduceQuantity(orderModel.getProductId(), orderModel.getQuantity());

        log.info("Placing order {} ", orderModel);

        // Order entity -> save the data with status orderModel created
        Orders orders = Orders.builder()
                .amount(orderModel.getAmount())
                .orderState("CREATED")
                .productId(orderModel.getProductId())
                .orderDate(Instant.now())
                .quantity(orderModel.getQuantity())
                .build();
        log.info("Saving order....{}", orders);
        ordersRepository.save(orders);
        log.info("Order saved");

        // Payment Service -> SUCCESS if complete else FAILED.
        log.info("Calling payment service to complete the payment...");
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(Long.valueOf(orders.getOrderId()))
                .paymentMode(orderModel.getPaymentMode())
                .amount(orderModel.getAmount())
                .build();

        // call the payment service. SUCCESS if complete else FAILED.
        String orderStatus = null;
        try {
            paymentService.doPayment(paymentRequest);
            orderStatus = "ORDER PLACED";
            log.info("Payment done successfully. Changing the order status...{}", orderStatus);
        } catch (Exception e) {
            orderStatus = "PAYMENT FAILED";
            log.info("Error occurred during payment...{}", orderStatus);
        }
        orders.setOrderState(orderStatus);
        ordersRepository.save(orders);
        log.info("Transaction done successfully!");
        return orderModel;
    }

    @Override
    public List<OrderModel> getAllOrders() {
        List<Orders> ordersList = ordersRepository.findAll();

        List<OrderModel> orderModels = ordersList.stream()
                .map(orders -> {
                    OrderModel orderModel = new OrderModel();
                    BeanUtils.copyProperties(orders, orderModel);
                    log.info("Retrieving orders...");
                    return orderModel;
                }).collect(Collectors.toList());
        log.info("Orders retrieved..");
        return orderModels;
    }

    @Override
    public void removeOrder(Integer id) {
        boolean isOrderExist = ordersRepository.existsById(id);
        if (isOrderExist)
            ordersRepository.deleteById(id);
        else
            throw new OrderNotFoundException("The order with id " + id + " is not available.");
    }

    // TODO: get specific order detail
    @Override
    public OrderResponse getOrderDetails(Long orderId) {
        log.info("Get order details for order Id: {}", orderId);

        Orders order = ordersRepository.findById(Math.toIntExact(orderId))
                .orElseThrow(() -> {
                    throw new CustomFeignErrorResponseException("Order not found ", "NOT_FOUND", 404);
                });

        log.info("Invoking Product service to fetch the product by Id: {}", order.getProductId());
        ProductResponse productResponse = restTemplate.getForObject(
                "http://PRODUCT-SERVICE/v1/products/" + order.getProductId(), ProductResponse.class);

        log.info("Getting payment information from the payment Service");
        PaymentResponse paymentResponse = restTemplate.getForObject(
                "http://PAYMENT-SERVICE/v1/payment/order/" + order.getProductId(),
                PaymentResponse.class);


        assert paymentResponse != null && productResponse != null;
        OrderResponse.PaymentDetails paymentDetails =
                OrderResponse.PaymentDetails.builder()
                        .paymentId(paymentResponse.getPaymentId())
                        .paymentStatus(paymentResponse.getStatus())
                        .paymentDate(paymentResponse.getPaymentDate())
                        .paymentMode(paymentResponse.getPaymentMode())
                        .build();

        OrderResponse.ProductDetails productDetails = OrderResponse.ProductDetails
                .builder()
                .productName(productResponse.getProductName())
                .productId(productResponse.getProductId())
                .price(productResponse.getPrice())
                .quantity(productResponse.getQuantity())
                .build();

        return OrderResponse.builder()
                .orderId(Long.valueOf(order.getOrderId()))
                .orderStatus(order.getOrderState())
                .amount(order.getAmount())
                .orderDate(order.getOrderDate())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();
    }
}
