package com.example.ratetracker.repository;

import com.example.ratetracker.entity.CurrencyEntity;
import com.example.ratetracker.entity.ExchangeRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExchangeRateEntityRepository extends JpaRepository<ExchangeRateEntity, Integer> {
    List<ExchangeRateEntity> findByBaseCurrency(CurrencyEntity baseCurrency);

}
