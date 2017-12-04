package com.ing.direct.transaction.repository;

import com.ing.direct.transaction.model.Transaction;
import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.ing.direct.common.utils.ClassUtils.toDate;
import static org.junit.Assert.*;

@ActiveProfiles({"test", "cacheDisabled"})
@SpringApplicationConfiguration(classes = TransactionRepositoryTest.TestAppConfig.class,
        initializers = ConfigFileApplicationContextInitializer.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TransactionRepositoryTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private MongodExecutable mongodExec;

    @Autowired
    private MongoClient mongoClient;

    private static MongodExecutable staticMongodExec;
    private static MongoClient staticMongoClient;


    private Transaction sampleTransaction = Transaction
            .builder()
            .customer("1")
            .date(toDate("1/10/2016 2:51:23 AM"))
            .amount(BigDecimal.valueOf(23.4))
            .description("first transaction description")
            .build();
//            .withId("1")
//            .withVersion(1l)
//            .withBody("transaction body")
//            .withDate(toDate("2016-10-01"))
//            .withTags(Arrays.asList("news", "sport"))
//            .withTitle("transaction title");


    @Before
    public void setUp() throws Exception {

        // init mongodb
        staticMongodExec = mongodExec;
        staticMongoClient = mongoClient;
    }


    @After
    public void tearDown() throws Exception {
        mongoTemplate.dropCollection(Transaction.class);
    }

    @AfterClass
    public static void postContruct() {
        // stop mongodb
        staticMongoClient.close();
        staticMongodExec.stop();
    }

    @Test
    public void shouldFindTransactionById() throws Exception {
        // given
        mongoTemplate.insert(sampleTransaction);

        // when
        List<Transaction> transactions = repository.findByCustomerId("1");

        // then
        assertFalse(transactions.isEmpty());
        assertNotNull(transactions.get(0));
        assertEquals("1", transactions.get(0).getCustomer());
        assertEquals(toDate("1/10/2016 2:51:23 AM"), transactions.get(0).getDate());
        assertEquals(BigDecimal.valueOf(23.4), transactions.get(0).getAmount());
        assertEquals("first transaction description", transactions.get(0).getDescription());
    }

    @Test
    public void shouldFindTransactionByCustomerIdAndDate() throws Exception {
        // given
        mongoTemplate.insert(sampleTransaction);

        // when
        Date start = toDate("1/10/2016 1:00:00 AM");
        Date end = toDate("1/10/2016 3:00:00 AM");
        List<Transaction> transactions = repository.findByCustomerIdAndDate("1", start, end);

        // then
        assertFalse(transactions.isEmpty());
        assertTrue(transactions.size() == 1);
        assertNotNull(transactions.get(0));
        assertEquals("1", transactions.get(0).getCustomer());
        assertEquals(toDate("1/10/2016 2:51:23 AM"), transactions.get(0).getDate());
        assertEquals(BigDecimal.valueOf(23.4), transactions.get(0).getAmount());
        assertEquals("first transaction description", transactions.get(0).getDescription());
    }

    @Test
    public void shouldNotFindTransactionByCustomerIdAndDate() throws Exception {
        // given
        mongoTemplate.insert(sampleTransaction);

        // when
        Date start = toDate("1/10/2016 3:00:00 AM");
        Date end = toDate("1/10/2016 4:00:00 AM");
        List<Transaction> transactions = repository.findByCustomerIdAndDate("1", start, end);

        // then
        assertTrue(transactions.isEmpty());
    }

    @Test
    public void shouldSaveOrUpdateTransaction() throws Exception {
        // given
        List<Transaction> transactionsBefore = mongoTemplate.findAll(Transaction.class);

        // when
        repository.saveOrUpdate(sampleTransaction);

        // then
        List<Transaction> transactionsAfter = mongoTemplate.findAll(Transaction.class);

        assertTrue(transactionsBefore.isEmpty());
        assertFalse(transactionsAfter.isEmpty());
        assertTrue(transactionsAfter.size() == 1);
        assertNotNull(transactionsAfter.get(0));
        assertEquals("1", transactionsAfter.get(0).getCustomer());
        assertEquals(toDate("1/10/2016 2:51:23 AM"), transactionsAfter.get(0).getDate());
        assertEquals(BigDecimal.valueOf(23.4), transactionsAfter.get(0).getAmount());
        assertEquals("first transaction description", transactionsAfter.get(0).getDescription());
    }


    @Configuration
    @EnableAutoConfiguration
    @EnableMongoRepositories(basePackages = "com.ing.direct.transaction.repository")
    @ComponentScan({
            "com.ing.direct.transaction.service",
            "com.ing.direct.common.service"
    })
    public static class TestAppConfig {
    }

}