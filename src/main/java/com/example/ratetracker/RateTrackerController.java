package com.example.ratetracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/exchange")
@RestController
public class RateTrackerController {
    @Autowired
    RatetrackerService ratetrackerService;
    @GetMapping("/")
    public String home() {
        return "Api works";
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
