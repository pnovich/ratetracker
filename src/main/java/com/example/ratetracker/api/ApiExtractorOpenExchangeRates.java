package com.example.ratetracker.api;

import com.example.ratetracker.dto.ApiResponseDto;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ApiExtractorOpenExchangeRates implements ApiExtractor {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String apiUrl;

    public ApiExtractorOpenExchangeRates(
            RestTemplate restTemplate,
            String apiKey,
            String apiUrl) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
    }

    @Override
    public Map<String, ApiResponseDto> getMapFromApi(Set<String> currencyValues) {
        Map<String, ApiResponseDto> map = new HashMap<>();

        for (String currency : currencyValues) {
            ApiResponseDto response = fetchExchangeRates(currency);
            map.put(currency, response);
        }
        return map;
    }

    private ApiResponseDto fetchExchangeRates(String baseCurrency) {
        String url = String.format("%s/latest.json?app_id=%s&base=%s", apiUrl, apiKey, baseCurrency);

        try {
            return restTemplate.getForObject(url, ApiResponseDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch exchange rates for " + baseCurrency, e);
        }
    }

}
