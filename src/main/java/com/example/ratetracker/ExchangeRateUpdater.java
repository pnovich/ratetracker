package com.example.ratetracker;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class ExchangeRateUpdater {

    private final RatetrackerService ratetrackerService;

    public ExchangeRateUpdater(RatetrackerService ratetrackerService) {
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
