package com.example.ratetracker.controller;

import com.example.ratetracker.dto.CurrencyRequestDto;
import com.example.ratetracker.dto.CurrencyResponseDto;
import com.example.ratetracker.dto.ExchangeRateResponseDto;
import com.example.ratetracker.service.RatetrackerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/exchange")
@RestController
public class RateTrackerController {
    private final RatetrackerService ratetrackerService;

    public RateTrackerController(RatetrackerService ratetrackerService) {
        this.ratetrackerService = ratetrackerService;
    }

    @GetMapping("/")
    public ResponseEntity<String> home() {
        return ResponseEntity.status(HttpStatus.CREATED).body("Api works");
    }

    @GetMapping("/rates/{currency}")
    public ResponseEntity<ExchangeRateResponseDto> getRatesForBase(
            @PathVariable String currency) {
        ExchangeRateResponseDto response = ratetrackerService.getRateForCurrency(currency);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @GetMapping("/currencies")
    public ResponseEntity<CurrencyResponseDto> getCurrencies() {

        CurrencyResponseDto response = ratetrackerService.getCurrencies();
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @PostMapping("/currencies")
    public ResponseEntity<String> postCurrencies(
            @RequestBody CurrencyRequestDto request) throws Exception {
        ratetrackerService.addCurrency(request.getCurrencyCode());
        return ResponseEntity.status(HttpStatus.CREATED).body("Currency added successfully");
    }

}
