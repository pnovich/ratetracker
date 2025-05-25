package com.example.ratetracker.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "exchange_rates")
public class ExchangeRateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "currency_code", nullable = false)
    private CurrencyEntity baseCurrency;

    @ElementCollection
    @CollectionTable(name = "exchange_rates_details", joinColumns = @JoinColumn(name = "rate_id"))
    @MapKeyColumn(name = "target_currency")
    @Column(name = "rate")
    private Map<String, Double> rates;

    private Instant timestamp;
    public Map<String, Double> getRates() {
        return rates;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CurrencyEntity getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(CurrencyEntity baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

}