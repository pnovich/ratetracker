package com.example.ratetracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ApiExtractorFromJson implements ApiExtractor {
    @Override
    public Map<String, ApiResponseDto> getMapFromApi(List<String> currencyValues) {
        Map<String, ApiResponseDto> map = new HashMap<>();
        for (String currencyValue : currencyValues) {
            ApiResponseDto dto = getApiResponseFromJson();
            dto.setBase(currencyValue);
            map.put(dto.getBase(), dto);
        }
        return map;
    }

    private ApiResponseDto getApiResponseFromJson() {
        final String JSON = "{ \"disclaimer\": \"Usage subject to terms: https://openexchangerates.org/terms\", \"license\": \"https://openexchangerates.org/license\", \"timestamp\": \"1748016000\", \"base\": \"USD\", \"rates\": { \"EUR\": 0.881106, \"GBP\": 0.740273 } }";

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(JSON, ApiResponseDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JSON", e);
        }

    }
}
