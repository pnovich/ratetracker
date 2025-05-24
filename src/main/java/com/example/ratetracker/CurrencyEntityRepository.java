package com.example.ratetracker;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyEntityRepository extends JpaRepository<CurrencyEntity, Integer> {
    CurrencyEntity findByCurrencyCode(String code);
}
