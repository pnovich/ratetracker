package com.example.ratetracker;

import java.util.Map;

public class ExchangeRateResponseDto {
    private String base;
    private Map<String, Double> rates;

    public ExchangeRateResponseDto(String base, Map<String, Double> rates) {
        this.base = base;
        this.rates = rates;
    }

    public ExchangeRateResponseDto() {
    }

    public String getBase() {
        return base;
    }

    public Map<String, Double> getRates() {
        return rates;
    }
}
