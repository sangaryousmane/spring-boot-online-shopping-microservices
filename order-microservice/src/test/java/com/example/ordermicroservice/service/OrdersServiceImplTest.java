package com.example.ordermicroservice.service;

import com.example.ordermicroservice.entities.Orders;
import com.example.ordermicroservice.exceptions.CustomFeignErrorResponseException;
import com.example.ordermicroservice.external.client.PaymentService;
import com.example.ordermicroservice.external.client.ProductService;
import com.example.ordermicroservice.external.request.PaymentRequest;
import com.example.ordermicroservice.external.response.PaymentResponse;
import com.example.ordermicroservice.external.response.ProductResponse;
import com.example.ordermicroservice.model.OrderModel;
import com.example.ordermicroservice.model.OrderResponse;
import com.example.ordermicroservice.model.PaymentMode;
import com.example.ordermicroservice.repository.OrdersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;

import static com.example.ordermicroservice.model.PaymentMode.APPLE_PAY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest
class OrdersServiceImplTest {

    @Mock
    private OrdersRepository ordersRepository;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ProductService productService;
    @Mock
    private PaymentService paymentService;

    @InjectMocks
    OrdersService ordersService = new OrdersServiceImpl();



    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }
    @DisplayName(value = "Get order success scenario")
    @Test
    void test_when_order_success() {
        // Mocking
        Orders order = getMockOrder();
        when(ordersRepository.findById(anyLong()))
                .thenReturn(Optional.of(getMockOrder()));

        // Mock product client
        when(restTemplate.getForObject("http://PRODUCT-SERVICE/v1/products/"+order.getProductId(),
                ProductResponse.class))
                .thenReturn(getProductResponse());

        // Mock payment client
        when(restTemplate.getForObject(
                "http://PAYMENT-SERVICE/v1/payment/order/" + order.getProductId(),
                PaymentResponse.class))
                .thenReturn(getPaymentResponse());

        // Actual
        OrderResponse orderResponse = ordersService.getOrderDetails(1L);


        // TODO: Verification, find order one time with any Long value.
        verify(ordersRepository, times(1)).findById(anyLong());
        verify(restTemplate, times(1)).getForObject(
                "http://PRODUCT-SERVICE/v1/products/" + order.getProductId(),
                ProductResponse.class);
        verify(restTemplate, times(1)).getForObject(
                "http://PAYMENT-SERVICE/v1/payment/order/" + order.getProductId(),
                PaymentResponse.class);

        //Assert
        assertNotNull(orderResponse);
        assertEquals(order.getOrderId(), orderResponse.getOrderId());
    }

    @DisplayName("Get orders - failure scenario")
    @Test
    void test_when_getOrder_Not_Found_then_Not_Found(){
        //TODO: It will throw an exception whenever you're getting null
        when(ordersRepository.findById(anyLong()))
                .thenReturn(Optional.empty());


        CustomFeignErrorResponseException
                feignException = assertThrows(CustomFeignErrorResponseException.class,
                ()->ordersService.getOrderDetails(1L));
        assertEquals("NOT_FOUND", feignException.getErrCode());
        assertEquals(404, feignException.getStatus());

        verify(ordersRepository, times(1))
                .findById(anyLong());
    }

    @DisplayName("Place Order - success scenario")
    @Test
    void test_when_placeOrder_success(){

        Orders order=getMockOrder();

        when(ordersRepository.save(any(Orders.class)))
                .thenReturn(order);

        // Test external product api
        when(productService.reduceQuantity(anyLong(), anyLong()))
                .thenReturn(new ResponseEntity<>(OK));

        // Test external payment api
        when(paymentService.doPayment(any(PaymentRequest.class)))
                .thenReturn(new ResponseEntity<Long>(1L, OK));

        OrderModel orderModel = ordersService.saveOrder(getMockOrderRequest());

        verify(ordersRepository, times(2))
                .save(any());
        verify(productService, times(1))
                .reduceQuantity(anyLong(), anyLong());
        verify(paymentService, times(1))
                .doPayment(any(PaymentRequest.class));


        // Do assertions
        assertEquals(order.getOrderId(), 1L);
    }

    @DisplayName("Place Order - payment failed scenario")
    @Test
    void test_when_place_order_payment_fails_then_order_is_placed(){
        Orders orders=getMockOrder();

        when(ordersRepository.save(any(Orders.class)))
                .thenReturn(orders);

        when(productService.reduceQuantity(anyLong(), anyLong()))
                .thenReturn(new ResponseEntity<>(OK));

        when(paymentService.doPayment(any(PaymentRequest.class)))
                .thenThrow(new RuntimeException());

        OrderModel orderModel = ordersService.saveOrder(getMockOrderRequest());

        verify(ordersRepository, times(2))
                .save(any());
        verify(productService, times(1))
                .reduceQuantity(anyLong(), anyLong());
        verify(paymentService, times(1))
                .doPayment(any(PaymentRequest.class));


        assertEquals(orders.getOrderId(), 1L);

    }

    private OrderModel getMockOrderRequest() {
        return OrderModel.builder()
                .productId(1L)
                .quantity(10L)
                .paymentMode(APPLE_PAY)
                .amount(100L)
                .build();
    }

    private PaymentResponse getPaymentResponse() {
        return PaymentResponse.builder()
                .paymentId(1L)
                .paymentDate(Instant.now())
                .paymentMode(APPLE_PAY)
                .OrderId(2L)
                .status("SUCCESS")
                .amount(10L)
                .build();
    }

    private ProductResponse getProductResponse() {
        return ProductResponse.builder()
                .productId(2L)
                .productName("Samsung")
                .quantity(3)
                .price(200)
                .build();
    }

    private Orders getMockOrder() {
        return Orders.builder()
                .orderId(1L).amount(100L)
                .productId(2L)
                .orderState("PLACED")
                .orderDate(Instant.now())
                .quantity(200L)
                .build();
    }


}