package com.example.ratetracker;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyEntityRepository extends JpaRepository<CurrencyEntity, Integer> {
    public CurrencyEntity findByCurrencyCode(String code);
}
