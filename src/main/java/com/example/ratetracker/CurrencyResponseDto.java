package com.example.ratetracker;

import java.util.List;

public class CurrencyResponseDto {
    private List<String> currencies;

    public CurrencyResponseDto(List<String> currencies) {
        this.currencies = currencies;
    }

    public List<String> getCurrencies() {
        return currencies;
    }
}