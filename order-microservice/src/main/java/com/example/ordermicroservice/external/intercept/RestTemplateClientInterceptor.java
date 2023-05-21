//package com.example.ordermicroservice.external.intercept;
//import org.springframework.http.HttpRequest;
//import org.springframework.http.client.ClientHttpRequestExecution;
//import org.springframework.http.client.ClientHttpRequestInterceptor;
//import org.springframework.http.client.ClientHttpResponse;
//import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
//import java.io.IOException;
//
//// TODO: this is use to provide support for RestTemplate in security
//public class RestTemplateClientInterceptor
//        implements ClientHttpRequestInterceptor {
//
//
//    private final OAuth2AuthorizedClientManager oAuth2ClientManager;
//
//    public RestTemplateClientInterceptor(
//            OAuth2AuthorizedClientManager oAuth2ClientManager) {
//        this.oAuth2ClientManager = oAuth2ClientManager;
//    }
//
//    @Override
//    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
//        request.getHeaders().add(
//                "Authorization",
//                "Bearer "+ oAuth2ClientManager
//                        .authorize(OAuth2AuthorizeRequest.withClientRegistrationId("internal-client")
//                                .principal("internal")
//                                .build())
//                        .getAccessToken().getTokenValue());
//        return execution.execute(request, body);
//    }
//}
