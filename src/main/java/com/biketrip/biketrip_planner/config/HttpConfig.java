//package com.biketrip.biketrip_planner.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.client.RestClient;
//
//@Configuration
//public class HttpConfig {
//    @Bean
//    public RestClient owmRestClient(@Value("${app.owm.base-url}") String baseUrl) {
//        return RestClient.builder().baseUrl(baseUrl).build();
//    }
//}
