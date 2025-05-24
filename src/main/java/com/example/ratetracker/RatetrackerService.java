package com.example.ratetracker;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class RatetrackerService {
    @Autowired
    CurrencyEntityRepository currencyEntityRepository;
    @Autowired
    ExchangeRateEntityRepository exchangeRateEntityRepository;
    @Autowired
    MapStorage mapStorage;
    private List<String> currencies = new ArrayList<>();
    private ExchangeRateResponseDto mockRates;

    @PostConstruct
    private void init() {
        currencies = new ArrayList<>();
        Map<String, Double> rates = new HashMap<>();
        rates.put("EUR", 1.1);
        rates.put("GBP", 1.2);
        rates.put("JPY", 0.9);
        String base = "USD";
        mockRates = new ExchangeRateResponseDto(base, rates);

    }
    public ExchangeRateResponseDto getRateForCurrency(String currency) {
//        return this.mockRates;
        if (!currencies.contains(currency)) {
            throw new RuntimeException("Currencies do not match");
        }
        ExchangeRateResponseDto reult = this.mapStorage.getStorage().get(currency);
        if (reult != null) {
            return reult;
        } else {
            throw new RuntimeException("Can`t find currencies for currency " + currency);
        }
    }

    public CurrencyResponseDto getCurrencies() {
        List<String> currencies = this.currencies;
        CurrencyResponseDto currencyResponseDto = new CurrencyResponseDto(currencies);
        return currencyResponseDto;
    }

    public List<String> getCurrencyValues() {
        return this.currencies;
    }

    public void addCurrency(String currency) {
        this.currencies.add(currency);
    }

    public void updateExchangeRates(Map<String,ApiResponseDto> apiResponseDtoMap) throws Exception {
       Map<String, ExchangeRateEntity> entities = new HashMap<>();
//       List<CurrencyEntity> currencies = this.currencyEntityRepository.findAll();
       for (Map.Entry<String, ApiResponseDto> entry : apiResponseDtoMap.entrySet()) {
           String currency = entry.getKey();
           ApiResponseDto apiResponseDto = entry.getValue();

           ExchangeRateEntity exchangeRateEntity = mapFromApiResponseToExchangeRateEntity(apiResponseDto);

           CurrencyEntity currencyEntity = this.currencyEntityRepository.findByCurrencyCode(currency);

           if (currencyEntity == null) {
               currencyEntity = new CurrencyEntity(currency);
               currencyEntityRepository.save(currencyEntity);
           }

           exchangeRateEntity.setBaseCurrency(currencyEntity);

           exchangeRateEntityRepository.save(exchangeRateEntity);
       }
    }

    public void updateMapStorageFromApiResponse(Map<String,ApiResponseDto> apiResponseDtoMap) throws Exception {

        Map<String, ExchangeRateResponseDto> ratesMap = mapFromApiResponseToExchangeRateDto(apiResponseDtoMap);
        this.mapStorage.refreshMap();
        this.mapStorage.setStorage(ratesMap);
        System.out.println("ratesMap: " + this.mapStorage.getStorage());
    }

    public  ExchangeRateEntity mapFromApiResponseToExchangeRateEntity(
            ApiResponseDto apiResponseDto)  {

        Map<String, Double> rates = apiResponseDto.getRates();

        ExchangeRateEntity exchangeRate = new ExchangeRateEntity();
        exchangeRate.setBaseCurrency(new CurrencyEntity(apiResponseDto.getBase()));
        exchangeRate.setRates(rates);
        exchangeRate.setTimestamp(Instant.ofEpochSecond(apiResponseDto.getTimestamp()));

        return exchangeRate;
    }

    public Map<String, ExchangeRateResponseDto> mapFromApiResponseToExchangeRateDto(
            Map<String, ApiResponseDto> apiResponseDtoMap) throws Exception {
        Map<String, ExchangeRateResponseDto> exchangeRateResponseDtoMap = new HashMap<>();
        for (Map.Entry<String, ApiResponseDto> entry : apiResponseDtoMap.entrySet()) {
            String currency = entry.getKey();
            ApiResponseDto apiResponseDto = entry.getValue();
            Map<String, Double> rates = apiResponseDto.getRates();
            ExchangeRateResponseDto exchangeRateResponseDto = new ExchangeRateResponseDto(currency, rates);
            exchangeRateResponseDtoMap.put(currency, exchangeRateResponseDto);
        }
        return exchangeRateResponseDtoMap;
    }

}
