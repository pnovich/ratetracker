package com.example.ratetracker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class ExchangeRateUpdater {

    private final RatetrackerService ratetrackerService;
    private final Logger logger = LoggerFactory.getLogger(ExchangeRateUpdater.class);

    public ExchangeRateUpdater(RatetrackerService ratetrackerService) {
        this.ratetrackerService = ratetrackerService;
    }

    @Scheduled(
//            fixedRate = 3600000
            fixedRate = 60000

    )
    public void doScheduledRatesFetching() throws Exception {
        logger.info("scheduled fetching");
        ratetrackerService.fetchExchangeRates();
    }

}
