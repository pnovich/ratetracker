package com.example.ratetracker;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MapStorage {
    private Map<String, ExchangeRateResponseDto> storage = new HashMap<>();

    public MapStorage() {
    }

    public MapStorage(Map<String, ExchangeRateResponseDto> storage) {
        this.storage = storage;
    }

    public Map<String, ExchangeRateResponseDto> getStorage() {
        return storage;
    }

    public void setStorage(Map<String, ExchangeRateResponseDto> storage) {
        this.storage = storage;
    }

    public void refreshMap() {
        this.storage.clear();
    }

}
