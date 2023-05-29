package com.example.ordermicroservice.service;

import com.example.ordermicroservice.entities.Orders;
import com.example.ordermicroservice.exceptions.CustomFeignErrorResponseException;
import com.example.ordermicroservice.exceptions.OrderNotFoundException;
import com.example.ordermicroservice.external.client.CustomerService;
import com.example.ordermicroservice.external.client.PaymentService;
import com.example.ordermicroservice.external.client.ProductService;
import com.example.ordermicroservice.external.request.PaymentRequest;
import com.example.ordermicroservice.external.response.PaymentResponse;
import com.example.ordermicroservice.external.response.ProductResponse;
import com.example.ordermicroservice.model.CustomerRequest;
import com.example.ordermicroservice.model.OrderModel;
import com.example.ordermicroservice.model.OrderResponse;
import com.example.ordermicroservice.repository.OrdersRepository;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@Log4j2
@NoArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${payment.baseURL}")
    private String basePaymentURL;

    @Value("${product.baseURL}")
    private String baseProductURL;

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
                .orderId(orders.getOrderId())
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
        sendEmailToCustomer(orders);
        return orderModel;
    }

    // TODO: send email to customer after order
    public void sendEmailToCustomer(Orders orders) {
        CustomerRequest getCusto = new CustomerRequest();

        // customer order done
        log.info("Customer ordering...");
        customerService.getCustomerDetails(getCusto.getCustomerId());
        Orders customerId = ordersRepository.findById(
                orders.getCustomerId()).orElseThrow(() ->
                new CustomFeignErrorResponseException("Customer Id not found", "404", 1));

        try {
            mailSender.send(mimeMessage -> {
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(getCusto.getCustomerEmail()));
                mimeMessage.setFrom(new InternetAddress("buyme@gmail.com"));
                String message = "Dear " + getCusto.getLastName() + " " +
                        getCusto.getFirstName() + ", thanks for your purchase." +
                        "Your order number is " + customerId.getOrderId() + orders.getOrderId() + ".";
                mimeMessage.setText(message);
            });
        } catch (MailException ex) {
            log.error("Unable to send mail {} ", ex.getMessage());
        }
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
    public void removeOrder(Long id) {
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

        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> {
                    throw new CustomFeignErrorResponseException("Order not found ", "NOT_FOUND", 404);
                });

        log.info("Invoking Product service to fetch the product by Id: {}", order.getProductId());
        ProductResponse productResponse = restTemplate.getForObject(
                baseProductURL + order.getProductId(), ProductResponse.class);

        log.info("Getting payment information from the payment Service");
        PaymentResponse paymentResponse = restTemplate.getForObject(
                basePaymentURL + order.getProductId(),
                PaymentResponse.class);

        assert paymentResponse != null;
        OrderResponse.PaymentDetails paymentDetails =
                OrderResponse.PaymentDetails.builder()
                        .paymentId(paymentResponse.getPaymentId())
                        .paymentStatus(paymentResponse.getStatus())
                        .paymentDate(paymentResponse.getPaymentDate())
                        .paymentMode(paymentResponse.getPaymentMode())
                        .build();
        assert productResponse != null;
        OrderResponse.ProductDetails productDetails = OrderResponse.ProductDetails
                .builder()
                .productName(productResponse.getProductName())
                .productId(productResponse.getProductId())
                .price(productResponse.getPrice())
                .quantity(productResponse.getQuantity())
                .build();
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .orderStatus(order.getOrderState())
                .amount(order.getAmount())
                .orderDate(order.getOrderDate())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();
    }
}
