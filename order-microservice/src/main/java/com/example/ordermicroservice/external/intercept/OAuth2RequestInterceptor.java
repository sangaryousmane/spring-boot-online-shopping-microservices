//package com.example.ordermicroservice.external.intercept;
//import feign.RequestInterceptor;
//import feign.RequestTemplate;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
//
//  this is use to provide support for RestTemplate in security
//@Configuration
//public class OAuth2RequestInterceptor implements RequestInterceptor {
//
//    private final OAuth2AuthorizedClientManager clientManager;
//
//    public OAuth2RequestInterceptor(OAuth2AuthorizedClientManager clientManager) {
//        this.clientManager = clientManager;
//    }
//
//    @Override
//    public void apply(RequestTemplate template) {
//        template.header(
//                "Authorization",
//                "Bearer "+ clientManager
//                                .authorize(OAuth2AuthorizeRequest.withClientRegistrationId("internal-client")
//                                        .principal("internal")
//                                        .build())
//                                .getAccessToken().getTokenValue());
//    }
//}
