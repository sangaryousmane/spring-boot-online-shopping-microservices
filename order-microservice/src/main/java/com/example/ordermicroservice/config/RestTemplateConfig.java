package com.example.ordermicroservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

//    private final ClientRegistrationRepository clientRepo;
//    private final OAuth2AuthorizedClientRepository auth2ClientRepo;
//
//    public RestTemplateConfig(ClientRegistrationRepository clientRepo,
//                              OAuth2AuthorizedClientRepository auth2ClientRepo) {
//        this.clientRepo = clientRepo;
//        this.auth2ClientRepo = auth2ClientRepo;
//    }

//    @Bean
//    @LoadBalanced
//    public RestTemplate restTemplate() {
////        RestTemplate template = new RestTemplate();
////        template.setInterceptors(List.of(
////                new RestTemplateClientInterceptor(
////                        clientManager(clientRepo, auth2ClientRepo))
////        ));
//        return new RestTemplate();
//    }

//    @Bean
//    public OAuth2AuthorizedClientManager clientManager(
//            ClientRegistrationRepository clientRepo,
//            OAuth2AuthorizedClientRepository auth2ClientRepo) {
//
//        // Client provider
//        OAuth2AuthorizedClientProvider oauthClientProvider =
//                OAuth2AuthorizedClientProviderBuilder
//                        .builder()
//                        .clientCredentials()
//                        .build();
//
//        // Build default authorize client manager
//        DefaultOAuth2AuthorizedClientManager oauth2Manager =
//                new DefaultOAuth2AuthorizedClientManager(
//                        clientRepo, auth2ClientRepo);
//        oauth2Manager.setAuthorizedClientProvider(oauthClientProvider);
//        return oauth2Manager;
//
//
//    }
}
