package senberg.musicmashup.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {
    @Value("${resttemplate.connect-timeout-ms}")
    private int connectTimeoutMS;
    @Value("${resttemplate.read-timeout-ms}")
    private int readTimeoutMS;

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.setConnectTimeout(Duration.ofMillis(connectTimeoutMS)).setReadTimeout(Duration.ofMillis(readTimeoutMS)).build();
    }
}
