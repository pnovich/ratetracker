package com.example.ratetracker.api;

import com.example.ratetracker.dto.ApiResponseDto;

import java.util.Map;
import java.util.Set;

public interface ApiExtractor {
    Map<String, ApiResponseDto> getMapFromApi(Set<String> currencyValues);
}
