package com.example.ratetracker;

import java.util.Map;
import java.util.Set;

public interface ApiExtractor {
    Map<String, ApiResponseDto> getMapFromApi(Set<String> currencyValues);
}
