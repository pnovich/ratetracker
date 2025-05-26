package com.example.ratetracker.api;

import com.example.ratetracker.dto.ApiResponseDto;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ApiExtractorOpenExchangeWebClientBased implements ApiExtractor {

    private final WebClient webClient;
    private final String apiKey;
    private final String apiUrl;

    public ApiExtractorOpenExchangeWebClientBased(WebClient.Builder webClientBuilder, String apiKey, String apiUrl) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
    }

    @Override
    public Map<String, ApiResponseDto> getMapFromApi(Set<String> currencyValues) {
        Map<String, ApiResponseDto> map = new HashMap<>();

        for (String currency : currencyValues) {
            ApiResponseDto response = fetchExchangeRates(currency).block(); // Blocking call for simplicity
            map.put(currency, response);
        }
        return map;
    }

    private Mono<ApiResponseDto> fetchExchangeRates(String baseCurrency) {
        String url = String.format("/latest.json?app_id=%s&base=%s", apiKey, baseCurrency);

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(ApiResponseDto.class)
                .onErrorResume(e -> {
                    throw new RuntimeException("Failed to fetch exchange rates for " + baseCurrency, e);
                });
    }
}