package com.example.ratetracker;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
public class ExchangeRateUpdater {

    private final RestTemplate restTemplate;
    private final RatetrackerService ratetrackerService;
    private final ApiExtractor extractor;

    public ExchangeRateUpdater(RestTemplate restTemplate, RatetrackerService ratetrackerService, ApiExtractor extractor) {
        this.restTemplate = restTemplate;
        this.ratetrackerService = ratetrackerService;
        this.extractor = extractor;
    }

    @Scheduled(
//            fixedRate = 3600000
            fixedRate = 15000

    )
    public void fetchExchangeRates() throws Exception {
        List<String> currencyValues = ratetrackerService.getCurrencyValues();
        Map<String,ApiResponseDto> apiResponseDtoMap = extractor.getMapFromApi(currencyValues);
        ratetrackerService.updateExchangeRates(apiResponseDtoMap);
        System.out.println("database updated");
        ratetrackerService.updateMapStorageFromApiResponse(apiResponseDtoMap);
        System.out.println("map updated");
    }

}
