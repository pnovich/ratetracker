package com.example.ratetracker;

import com.example.ratetracker.api.ApiExtractor;
import com.example.ratetracker.api.ApiExtractorFromJson;
import com.example.ratetracker.api.ApiExtractorOpenExchangeRates;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Value("${exchange.api.key}")
    private String apiKey;

    @Value("${exchange.api.url}")
    private String apiUrl;

    @Bean
    public ApiExtractor apiExtractor() {
        if (apiKey != null && !apiKey.isEmpty()) {
            return new ApiExtractorOpenExchangeRates(restTemplate(),
                    apiKey, apiUrl);
        } else {
            return new ApiExtractorFromJson();
        }
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RateTracker API")
                        .version("1.0")
                        .description("API for managing exchange rates"));
    }

}
