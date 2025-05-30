package com.example.ratetracker.dto;

import java.util.Set;

public class CurrencyResponseDto {
    private Set<String> currencies;

    public CurrencyResponseDto(Set<String> currencies) {
        this.currencies = currencies;
    }

    public Set<String> getCurrencies() {
        return currencies;
    }

}