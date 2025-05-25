
# RateTracker: PostgreSQL, Docker & Flyway Setup

## Prerequisites
Before running the project, ensure you have:
- **Docker** → To run PostgreSQL as a container.
- **Java 17+** → Required for Spring Boot.
- **Maven** → Dependency management & building.
- **PostgreSQL JDBC Driver** → Enables database connectivity.

---  

## Running PostgreSQL via Docker
### Start the container:
```bash
docker-compose up -d

RateTracker
Description
RateTracker is a currency exchange rate tracking system built with Spring Boot, PostgreSQL, and Docker.
It allows users to:
- Add currencies
- Retrieve exchange rates
- Store historical rate data
The system uses Flyway for database migrations and integrates external exchange rate APIs to keep data up-to-date.
Designed for performance, it leverages caching with MapStorage to optimize rate retrieval.
Notes
- When a user adds a new currency, a request to the API is triggered,
updating the internal map immediately.
- Exchange Rate API Behavior Based on API Key
- If the application is configured with a free account API key, exchange rates can only be fetched with USD as the base currency.
- Consequently, users can only add USD as a currency via the POST /api/exchange/currencies endpoint.
- Since exchange rates are retrieved only for user-added currencies, and non-USD currencies cannot be added, the response from GET /api/exchange/rates/USD will contain an empty set of rates.
- To fully utilize the exchange rate tracking functionality, it is recommended to use a paid account API key, which allows fetching exchange rates for multiple base currencies.
- If a paid key is unavailable, consider not providing an API key. In this case, the application will default to using the mocked exchange rates from JSON (ApiExtractorFromJson).
