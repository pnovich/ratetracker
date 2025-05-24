package com.example.ratetracker.repository;

import com.example.ratetracker.entity.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyEntityRepository extends JpaRepository<CurrencyEntity, Integer> {
    CurrencyEntity findByCurrencyCode(String code);
}
