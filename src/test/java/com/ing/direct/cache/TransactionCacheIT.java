package com.ing.direct.cache;

import com.google.common.collect.Lists;
import com.ing.direct.common.utils.ClassUtils;
import com.ing.direct.transaction.model.Transaction;
import com.ing.direct.transaction.repository.TransactionRepository;
import com.ing.direct.transaction.service.TransactionService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.ing.direct.common.utils.ClassUtils.toDate;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles({"test"})
@SpringApplicationConfiguration(classes = TransactionCacheIT.TestCacheConfig.class,
        initializers = ConfigFileApplicationContextInitializer.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TransactionCacheIT {

    @Autowired
    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository repository;

    @Autowired
    private CacheManager cacheManager;

    private Transaction sampleTransaction = Transaction
            .builder()
            .customer("1")
            .date(toDate("1/10/2016 2:51:23 AM"))
            .amount(BigDecimal.valueOf(12.3))
            .description("transaction description")
            .build();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        transactionService.save(sampleTransaction);
        transactionService.setRepository(repository);
    }

    @After
    public void tearDown() throws Exception {
        // reset transaction cache
        cacheManager.getCache(ClassUtils.TRANSACTIONS_COLLECTION_NAME).clear();
    }

    @Test
    @DirtiesContext
    public void shouldFindTransactionByIdFromCache() {

        // given
        when(repository.findById(any(String.class)))
                .thenReturn(Optional.of(sampleTransaction));

        String id = "1";
        Cache transactionCache = cacheManager.getCache(ClassUtils.TRANSACTIONS_COLLECTION_NAME);
        Cache.ValueWrapper beforeFillingCache = transactionCache.get(id);


        // when
        Optional<Transaction> transactionFromRepo = transactionService.findById(id);
        Optional<Transaction> transactionFromCache = transactionService.findById(id);

        // then
        Cache.ValueWrapper afterFillingCache = transactionCache.get(id);
        assertNull(beforeFillingCache);
        assertNotNull(afterFillingCache);
        assertTrue(transactionFromRepo.isPresent());
        assertTrue(transactionFromCache.isPresent());
        assertEquals(transactionFromCache, transactionFromRepo);

        verify(repository, times(1)).findById(any(String.class));
    }

    @Test
    @DirtiesContext
    public void shouldFindAllTransactionsFromCache() {

        // given
        when(repository.findAll()).thenReturn(
                Lists.newArrayList(sampleTransaction));

        // when
        List<Transaction> transactionsFromRepo = transactionService.findAll();
        List<Transaction> transactionsFromCache = transactionService.findAll();

        // then
        assertFalse(transactionsFromRepo.isEmpty());
        assertFalse(transactionsFromCache.isEmpty());
        assertEquals(transactionsFromCache, transactionsFromRepo);

        verify(repository, times(1)).findAll();

    }

    @Configuration
    @ComponentScan({"com.ing.direct.transaction.service", "com.ing.direct.transaction.model"})
    @EnableAutoConfiguration
    @EnableCaching
    public static class TestCacheConfig {
        @Bean
        CacheManager cacheManager() {
            return new ConcurrentMapCacheManager(ClassUtils.TRANSACTIONS_COLLECTION_NAME);
        }
    }

}
