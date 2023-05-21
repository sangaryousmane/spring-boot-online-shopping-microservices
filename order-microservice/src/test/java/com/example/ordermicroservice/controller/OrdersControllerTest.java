package com.example.ordermicroservice.controller;


import com.example.ordermicroservice.OrderServiceConfig;
import com.example.ordermicroservice.entities.Orders;
import com.example.ordermicroservice.model.PaymentMode;
import com.example.ordermicroservice.model.request.OrderRequest;
import com.example.ordermicroservice.repository.OrdersRepository;
import com.example.ordermicroservice.service.OrdersService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
//TODO: this is an integration test class


@SpringBootTest({"server.port=0"})
@EnableConfigurationProperties
@AutoConfigureMockMvc
@ContextConfiguration(classes = {OrderServiceConfig.class})
public class OrdersControllerTest {


    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private MockMvc mockMvc;

    @RegisterExtension
    static WireMockExtension wireMockServer =
            WireMockExtension.newInstance()
                    .options(WireMockConfiguration.wireMockConfig().port(8760)).build();

    private ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


    @BeforeEach
    void setUp() throws IOException {
        getProductDetailsResponse();
        doPayment();
        getPaymentDetails();
        reduceQuantity();
    }

    private void doPayment() {
        wireMockServer.stubFor(post(urlEqualTo("/v1/payment/makePayment"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)));
    }

    private void reduceQuantity() {
        wireMockServer.stubFor(put(urlMatching(
                "/product/reduceQuantity/.*"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)));
    }

    private void getPaymentDetails() throws IOException {
        wireMockServer.stubFor(get(urlMatching("/v1/payment/.*"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody(StreamUtils.copyToString(OrdersControllerTest.class.getClassLoader()
                                .getResourceAsStream("mock/GetPayment.json"), Charset.defaultCharset()))));
    }

    private void getProductDetailsResponse() throws IOException {
        // GET /v1/product/1
        wireMockServer.stubFor(get("/v1/products/1")
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody(StreamUtils.copyToString(OrdersControllerTest.class.getClassLoader()
                                .getResourceAsStream("mock/GetProducts.json"), Charset.defaultCharset()))));
    }

//    @Test
//    void test_when_placeOrder_doPayment_success() throws Exception {
//        // First place order.
//        OrderRequest orderRequest = getMockOrderRequest();
//        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/v1/orders/save")
////                        .with(jwt().authorities(new SimpleGrantedAuthority("CUSTOMER")))
//                                .contentType(APPLICATION_JSON_VALUE)
//                                .content(objectMapper.writeValueAsString(orderRequest))
//                ).andExpect(MockMvcResultMatchers.status().isOk())
//                .andReturn();
//
//        String orderId = mvcResult.getResponse().getContentAsString();
//        Optional<Orders> order = ordersRepository.findById(Long.valueOf(orderId));
//        assertTrue(order.isPresent());
//
//        // Get order by id from DB and check.
//        // Check Output
//        Orders o = order.get();
//        assertEquals(Long.parseLong(orderId), o.getOrderId());
//        assertEquals("PLACED", o.getOrderState());
//        assertEquals(orderRequest.getTotalAmount(), o.getAmount());
//        assertEquals(orderRequest.getQuantity(), o.getQuantity());
//
//
//    }

    private OrderRequest getMockOrderRequest() {
        return OrderRequest.builder()
                .productId(1L)
                .paymentMode(PaymentMode.CASH)
                .quantity(10)
                .totalAmount(200)
                .build();
    }
}