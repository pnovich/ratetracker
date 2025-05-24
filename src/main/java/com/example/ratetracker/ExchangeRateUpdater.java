package com.example.ratetracker;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class ExchangeRateUpdater {

    private final RatetrackerService ratetrackerService;

    public ExchangeRateUpdater(RestTemplate restTemplate, RatetrackerService ratetrackerService, ApiExtractor extractor) {
        this.ratetrackerService = ratetrackerService;
    }

    @Scheduled(
//            fixedRate = 3600000
            fixedRate = 60000

    )
    public void doScheduledRatesFetching() throws Exception {
        System.out.println("scheduled fetching");
        ratetrackerService.fetchExchangeRates();
    }

}
