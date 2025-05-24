package com.example.ratetracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ApiExtractorFromJson implements ApiExtractor {
    @Override
    public Map<String, ApiResponseDto> getMapFromApi(List<String> currencyValues) {
        Map<String, ApiResponseDto> map = new HashMap<>();
        for (String currencyValue : currencyValues) {
            ApiResponseDto dto = getApiResponseFromJson();
            dto.setBase(currencyValue);
            map.put(dto.getBase(), dto);
        }
        return map;
    }

    private ApiResponseDto getApiResponseFromJson() {
//        final String JSON = "{ \"disclaimer\": \"Usage subject to terms: https://openexchangerates.org/terms\", \"license\": \"https://openexchangerates.org/license\", \"timestamp\": \"1748016000\", \"base\": \"USD\", \"rates\": { \"EUR\": 0.881106, \"GBP\": 0.740273 } }";
        final String JSON = "{ \"disclaimer\": \"Usage subject to terms: https://openexchangerates.org/terms\", \"license\": \"https://openexchangerates.org/license\", \"timestamp\": \"1748016000\", \"base\": \"USD\", \"rates\": { \"AED\": 3.672965, \"AFN\": 69.963291, \"ALL\": 86.535373, \"AMD\": 383.667899, \"ANG\": 1.79, \"AOA\": 911.9551, \"ARS\": 1133.009921, \"AUD\": 1.544191, \"AWG\": 1.80125, \"AZN\": 1.7, \"BAM\": 1.723787, \"BBD\": 2, \"BDT\": 121.844574, \"BGN\": 1.7233, \"BHD\": 0.377036, \"BIF\": 2977.149131, \"BMD\": 1, \"BND\": 1.286837, \"BOB\": 6.912782, \"BRL\": 5.68825, \"BSD\": 1, \"BTC\": 0.000009161295, \"BTN\": 85.272842, \"BWP\": 13.428054, \"BYN\": 3.273766, \"BZD\": 2.009404, \"CAD\": 1.375755, \"CDF\": 2900.945781, \"CHF\": 0.821476, \"CLP\": 941.06, \"CNY\": 7.1806, \"COP\": 4177.4869, \"CRC\": 508.829622, \"CZK\": 21.9058, \"DKK\": 6.57261, \"DOP\": 59.078351, \"DZD\": 132.386926, \"EGP\": 49.889421, \"ERN\": 15, \"EUR\": 0.881106, \"GBP\": 0.740273, \"UAH\": 41.523021, \"USD\": 1, \"ZAR\": 17.853074, \"ZMW\": 27.359384, \"ZWL\": 322 } }";

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(JSON, ApiResponseDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JSON", e);
        }

    }
}
