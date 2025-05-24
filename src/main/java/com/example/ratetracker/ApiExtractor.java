package com.example.ratetracker;

import java.util.List;
import java.util.Map;

public interface ApiExtractor {
    Map<String, ApiResponseDto> getMapFromApi(List<String> currencyValues);
}
