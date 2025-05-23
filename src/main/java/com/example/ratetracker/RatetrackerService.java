package com.example.ratetracker;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RatetrackerService {
    private List<String> currencies = new ArrayList<>();
    private ExchangeRateResponseDto mockRates;

    @PostConstruct
    private void init() {
        currencies = new ArrayList<>();
        Map<String, Double> rates = new HashMap<>();
        rates.put("EUR", 1.1);
        rates.put("GBP", 1.2);
        rates.put("JPY", 0.9);
        String base = "USD";
        mockRates = new ExchangeRateResponseDto(base, rates);

    }
    public ExchangeRateResponseDto getRateForCurrency(String currency) {
        return this.mockRates;
    }

    public CurrencyResponseDto getCurrencies() {
        List<String> currencies = this.currencies;
        CurrencyResponseDto currencyResponseDto = new CurrencyResponseDto(currencies);
        return currencyResponseDto;
    }

    public void addCurrency(String currency) {
        this.currencies.add(currency);
    }
}
