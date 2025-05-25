package com.example.ratetracker.integration;

import com.example.ratetracker.dto.ExchangeRateResponseDto;
import com.example.ratetracker.entity.CurrencyEntity;
import com.example.ratetracker.entity.ExchangeRateEntity;
import com.example.ratetracker.repository.CurrencyEntityRepository;
import com.example.ratetracker.repository.ExchangeRateEntityRepository;
import com.example.ratetracker.service.RatetrackerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureMockMvc
public class RateTrackerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurrencyEntityRepository currencyRepository;

    @Autowired
    private ExchangeRateEntityRepository exchangeRateRepository;

    @Autowired
    private RatetrackerService ratetrackerService;


    @Test
    void testAddingCurrencyAndRetrievingRates() throws Exception {
        mockMvc.perform(post("/api/exchange/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"currencyCode\":\"EUR\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/exchange/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"currencyCode\":\"USD\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/exchange/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"currencyCode\":\"USD\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/exchange/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"currencyCode\":\"GBP\"}"))
                .andExpect(status().isCreated());

        assertTrue(currencyRepository.findByCurrencyCode("USD") != null);

        List<ExchangeRateEntity> usdRates = exchangeRateRepository.findByBaseCurrency(currencyRepository.findByCurrencyCode("USD"));
        assertFalse(usdRates.isEmpty());

        MvcResult result = mockMvc.perform(get("/api/exchange/rates/USD"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ExchangeRateResponseDto responseDto = new ObjectMapper().readValue(jsonResponse, ExchangeRateResponseDto.class);

        assertEquals("USD", responseDto.getName());
        LinkedHashMap<String,Double> expectedRates = new LinkedHashMap<>(){{
            put("EUR",0.881106);
            put("GBP",0.740273);
        }};
        assertEquals(expectedRates,responseDto.getRates());

        // Verify that currencies added by the user have been saved in the databasee
        List<CurrencyEntity> currencies = currencyRepository.findAll();
        assertEquals(3, currencies.size()); // Should be exactly 3, avoiding duplication

        // Verify that MapStorage has been updated
        ExchangeRateResponseDto cachedRates = ratetrackerService.getMapStorage().getStorage().get("USD");
        assertNotNull(cachedRates);
        assertFalse(cachedRates.getRates().isEmpty()); // Must contain stored exchange rates
    }

}
