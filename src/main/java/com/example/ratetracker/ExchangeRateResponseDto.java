package com.example.ratetracker;

import java.util.Map;

public class ExchangeRateResponseDto {
    private String name;
    private Map<String, Double> rates;

    public ExchangeRateResponseDto(String name, Map<String, Double> rates) {
        this.name = name;
        this.rates = rates;
    }

    public ExchangeRateResponseDto() {
    }

    public String getName() {
        return name;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    @Override
    public String toString() {
        return "ExchangeRateResponseDto{" +
                "name='" + name + '\'' +
                ", rates=" + rates +
                '}';
    }

}
