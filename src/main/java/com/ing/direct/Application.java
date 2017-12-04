package com.ing.direct;

import com.google.common.primitives.Ints;
import com.ing.direct.transaction.model.Transaction;
import com.ing.direct.transaction.repository.TransactionRepository;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.stream.Collectors;

import static com.ing.direct.common.utils.ClassUtils.toDate;

/**
 * Main Spring Boot Application class. Note that a {@link CommandLineRunner} is created
 * to initialize the Mongo database with a set of transactions.
 */
@EnableMongoRepositories(basePackages = "com.ing.direct.transaction.repository")
@SpringBootApplication
public class Application {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static final void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Initializes the Mongodb database with a set of transactions from data.txt file.
     *
     * @param transactionRepository Transaction repository to save the transactions
     * @return CommandLineRunner
     */
    @Bean
    CommandLineRunner init(TransactionRepository transactionRepository) throws URISyntaxException, IOException {

        LOG.info("start initializing mongodb...");

        try {
            return (evt) -> transactionRepository.saveOrUpdate(

                    new BufferedReader(
                            new InputStreamReader(ResourceUtils.getURL("classpath:data.txt").openStream()))
                            .lines()
                            .map(s -> s.split(","))
                            .filter(s -> !ArrayUtils.isEmpty(s) && s.length == 4 && Ints.tryParse(s[0]) != null)
                            .map(s -> Transaction
                                    .builder()
                                    .customer(s[0])
                                    .date(toDate(s[1]))
                                    .amount(new BigDecimal(s[2]))
                                    .description(s[3])
                                    .build()
                            )
                            .collect(Collectors.toList()).toArray(new Transaction[]{})
            );
        } finally {
            LOG.info("end of mongodb initialization...");
        }
    }

}
