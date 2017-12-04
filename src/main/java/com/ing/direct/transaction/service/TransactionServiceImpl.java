package com.ing.direct.transaction.service;

import com.ing.direct.transaction.model.Transaction;
import com.ing.direct.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.ing.direct.common.utils.ClassUtils.TRANSACTIONS_COLLECTION_NAME;

/**
 * Service class providing CRUD operations and caching logic for {@link Transaction} resource.
 *
 * @see TransactionRepository
 */
@Service
@CacheConfig(cacheNames = TRANSACTIONS_COLLECTION_NAME)
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository repository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @CacheEvict(allEntries = true)
    public void save(Transaction transaction) {
        repository.saveOrUpdate(transaction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable
    public Optional<Transaction> findById(String id) {
        return repository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable
    public List<Transaction> findByCustomerIdAndDate(String customerId, Date start, Date end) {
        return repository.findByCustomerIdAndDate(customerId, start, end);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable
    public List<Transaction> findByCustomerId(String customerId) {
        return repository.findByCustomerId(customerId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable
    public List<Transaction> findAll() {
        return repository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable
    public List<Transaction> findAll(Pageable pageable) {
        Page<Transaction> transactions = repository.findAll(pageable);
        return transactions.getContent();
    }

    /**
     * Sets an {@link TransactionRepository} instance.
     *
     * @param repository Transaction repository instance
     */
    public void setRepository(TransactionRepository repository) {
        this.repository = repository;
    }
}
