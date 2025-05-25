CREATE TABLE currencies (
                            currency_code VARCHAR PRIMARY KEY
);

CREATE TABLE exchange_rates (
                                id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                currency_code VARCHAR NOT NULL REFERENCES currencies(currency_code),
                                timestamp TIMESTAMP NOT NULL
);


CREATE TABLE exchange_rates_details (
                                        rate_id BIGINT REFERENCES exchange_rates(id),
                                        target_currency VARCHAR NOT NULL,
                                        rate FLOAT(53) NOT NULL

);