package com.example.ratetracker;

import jakarta.persistence.*;

@Entity
@Table(name = "currencies")
public class CurrencyEntity {

    @Id
    private String currencyCode;

    public CurrencyEntity(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public CurrencyEntity() {

    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

}
