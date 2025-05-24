package com.example.ratetracker.repository;

import com.example.ratetracker.entity.ExchangeRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateEntityRepository extends JpaRepository<ExchangeRateEntity, Integer> {
}
