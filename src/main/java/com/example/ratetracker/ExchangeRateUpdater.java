package com.example.ratetracker;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        Map<String, ApiResponseDto> filteredMap = filterMap(apiResponseDtoMap,currencyValues);
        ratetrackerService.updateExchangeRates(filteredMap);
        System.out.println("database updated");
        ratetrackerService.updateMapStorageFromApiResponse(filteredMap);
        System.out.println("map updated");
    }

    private Map<String,ApiResponseDto> filterMap(Map<String,ApiResponseDto> inputMap,
                                                 List<String> currencies) {
        Map<String, ApiResponseDto> filteredMap =
                new HashMap<>();
        for (Map.Entry<String, ApiResponseDto> dtoInputMapEntry : inputMap.entrySet()) {
            ApiResponseDto current = dtoInputMapEntry.getValue();
            ApiResponseDto newDto = new ApiResponseDto();
            newDto.setBase(current.getBase());
            newDto.setDisclaimer(current.getDisclaimer());
            newDto.setLicense(current.getLicense());
            newDto.setTimestamp(current.getTimestamp());
            Map<String, Double> newRates = current.getRates().entrySet()
                    .stream()
                    .filter(e -> currencies.contains(e.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            newDto.setRates(newRates);
            filteredMap.put(dtoInputMapEntry.getKey(), newDto);
        }
        return filteredMap;
    }

}
