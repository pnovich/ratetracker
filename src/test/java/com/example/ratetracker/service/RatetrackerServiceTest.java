package com.example.ratetracker.service;

import com.example.ratetracker.dto.ApiResponseDto;
import com.example.ratetracker.dto.ExchangeRateResponseDto;
import com.example.ratetracker.entity.CurrencyEntity;
import com.example.ratetracker.entity.ExchangeRateEntity;
import com.example.ratetracker.repository.CurrencyEntityRepository;
import com.example.ratetracker.repository.ExchangeRateEntityRepository;
import com.example.ratetracker.service.RatetrackerService;
import com.example.ratetracker.api.ApiExtractorFromJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatetrackerServiceTest {

    @Mock
    private CurrencyEntityRepository currencyRepository;
    @Mock
    private ExchangeRateEntityRepository exchangeRateRepository;
    @Mock
    private ApiExtractorFromJson apiExtractor;

    private MapStorage mapStorage;
    private RatetrackerService ratetrackerService;

    @BeforeEach
    void setUp() {
        mapStorage = new MapStorage();
        ratetrackerService = new RatetrackerService(currencyRepository, exchangeRateRepository, mapStorage, apiExtractor);
    }


    @Test
    void testFetchExchangeRates_updatesMapStorage() throws Exception {
        Map<String, Double> rates = Map.of("EUR", 0.88, "GBP", 0.74);
        ApiResponseDto mockApiResponse = new ApiResponseDto("Test disclaimer", "Test license", 1629988800L, "USD", rates);
        Map<String, ApiResponseDto> mockRates = Map.of("USD", mockApiResponse);

        when(apiExtractor.getMapFromApi(any())).thenReturn(mockRates);
        ratetrackerService.addCurrency("EUR");
        ratetrackerService.addCurrency("USD");
        ratetrackerService.addCurrency("GBP");

        ratetrackerService.fetchExchangeRates();

        ExchangeRateResponseDto cachedRates = ratetrackerService.getMapStorage().getStorage().get("USD");
        assertNotNull(cachedRates);
        assertEquals("USD", cachedRates.getName());
        assertEquals(rates, cachedRates.getRates());
    }

    @Test
    void testFetchExchangeRates_triggersDatabaseUpdate() throws Exception {
        when(apiExtractor.getMapFromApi(any())).thenReturn(Map.of(
                "USD", new ApiResponseDto("Disclaimer", "License", 1629988800L, "USD", Map.of("EUR", 0.88))
        ));

        ratetrackerService.fetchExchangeRates();

        verify(exchangeRateRepository, times(1)).save(any(ExchangeRateEntity.class));
    }

    @Test
    void testFetchExchangeRates_correctlyFiltersCurrencies() throws Exception {
        Map<String, Double> ratesUsd = Map.of("EUR", 0.88, "GBP", 0.74);
        ApiResponseDto usdResponse = new ApiResponseDto("Disclaimer", "License", 1629988800L, "USD", ratesUsd);

        when(apiExtractor.getMapFromApi(any())).thenReturn(Map.of("USD", usdResponse));

        ratetrackerService.addCurrency("EUR");
        ratetrackerService.addCurrency("USD");
        ratetrackerService.addCurrency("GBP");

        ratetrackerService.fetchExchangeRates();

        ExchangeRateResponseDto cachedRates = ratetrackerService.getMapStorage().getStorage().get("USD");
        assertNotNull(cachedRates);
        System.out.println(cachedRates);
        assertTrue(cachedRates.getRates().containsKey("EUR"));
        assertTrue(cachedRates.getRates().containsKey("GBP"));
    }

}