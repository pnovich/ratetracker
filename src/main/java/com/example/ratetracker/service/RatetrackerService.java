package com.example.ratetracker.service;

import com.example.ratetracker.api.ApiExtractor;
import com.example.ratetracker.dto.ApiResponseDto;
import com.example.ratetracker.dto.CurrencyResponseDto;
import com.example.ratetracker.dto.ExchangeRateResponseDto;
import com.example.ratetracker.entity.CurrencyEntity;
import com.example.ratetracker.entity.ExchangeRateEntity;
import com.example.ratetracker.repository.CurrencyEntityRepository;
import com.example.ratetracker.repository.ExchangeRateEntityRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RatetrackerService {
    private final CurrencyEntityRepository currencyEntityRepository;
    private final ExchangeRateEntityRepository exchangeRateEntityRepository;
    private final MapStorage mapStorage;
    private final ApiExtractor extractor;
    private final Set<String> currencies = new HashSet<>();

    public RatetrackerService(CurrencyEntityRepository currencyEntityRepository, ExchangeRateEntityRepository exchangeRateEntityRepository, MapStorage mapStorage, ApiExtractor extractor) {
        this.currencyEntityRepository = currencyEntityRepository;
        this.exchangeRateEntityRepository = exchangeRateEntityRepository;
        this.mapStorage = mapStorage;
        this.extractor = extractor;
    }

    public ExchangeRateResponseDto getRateForCurrency(String currency) {
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
        Set<String> currencies = this.currencies;
        CurrencyResponseDto currencyResponseDto = new CurrencyResponseDto(currencies);
        return currencyResponseDto;
    }

    public Set<String> getCurrencyValues() {
        return this.currencies;
    }

    public void addCurrency(String currency) throws Exception {
        this.currencies.add(currency);
        this.fetchExchangeRates();
    }

    public void fetchExchangeRates() throws Exception {
        Set<String> currencyValues = this.getCurrencyValues();
        Map<String, ApiResponseDto> apiResponseDtoMap = extractor.getMapFromApi(currencyValues);
        Map<String, ApiResponseDto> filteredMap = filterMap(apiResponseDtoMap, currencyValues);
        this.updateExchangeRatesInDatabase(filteredMap);
        System.out.println("database updated");
        this.updateMapStorageFromApiResponse(filteredMap);
        System.out.println("map updated");
    }


    private void updateExchangeRatesInDatabase(Map<String, ApiResponseDto> apiResponseDtoMap) {
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

    private void updateMapStorageFromApiResponse(Map<String, ApiResponseDto> apiResponseDtoMap) throws Exception {

        Map<String, ExchangeRateResponseDto> ratesMap = mapFromApiResponseToExchangeRateDto(apiResponseDtoMap);
        this.mapStorage.refreshMap();
        this.mapStorage.setStorage(ratesMap);
        System.out.println("ratesMap: " + this.mapStorage.getStorage());
    }

    private ExchangeRateEntity mapFromApiResponseToExchangeRateEntity(
            ApiResponseDto apiResponseDto) {

        Map<String, Double> rates = apiResponseDto.getRates();

        ExchangeRateEntity exchangeRate = new ExchangeRateEntity();
        exchangeRate.setBaseCurrency(new CurrencyEntity(apiResponseDto.getBase()));
        exchangeRate.setRates(rates);
        exchangeRate.setTimestamp(Instant.ofEpochSecond(apiResponseDto.getTimestamp()));

        return exchangeRate;
    }

    private Map<String, ExchangeRateResponseDto> mapFromApiResponseToExchangeRateDto(
            Map<String, ApiResponseDto> apiResponseDtoMap) {
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

    private Map<String, ApiResponseDto> filterMap(Map<String, ApiResponseDto> inputMap,
                                                  Set<String> currencies) {
        Map<String, ApiResponseDto> filteredMap =
                new HashMap<>();
        for (Map.Entry<String, ApiResponseDto> dtoInputMapEntry : inputMap.entrySet()) {
            ApiResponseDto current = dtoInputMapEntry.getValue();
            ApiResponseDto newDto = new ApiResponseDto();
            newDto.setBase(current.getBase());
            newDto.setDisclaimer(current.getDisclaimer());
            newDto.setLicense(current.getLicense());
            newDto.setTimestamp(current.getTimestamp());
            Map<String, Double> newRates = current.getRates().entrySet()
                    .stream()
                    .filter(e -> currencies.contains(e.getKey()))
                    .filter(e -> !Objects.equals(e.getKey(), current.getBase()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            newDto.setRates(newRates);
            filteredMap.put(dtoInputMapEntry.getKey(), newDto);
        }
        return filteredMap;
    }

}
