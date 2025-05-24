package com.example.ratetracker;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateEntityRepository extends JpaRepository<ExchangeRateEntity, Integer> {
}
