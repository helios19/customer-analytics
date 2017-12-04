package com.ing.direct.transaction.service;

import com.ing.direct.transaction.model.Transaction;
import com.ing.direct.transaction.repository.TransactionRepository;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service interface providing method declarations for CRUD operations for the {@link Transaction} resource.
 *
 * @see Transaction
 * @see TransactionServiceImpl
 */
public interface TransactionService {
    /**
     * Saves an {@link Optional<Transaction>} instance.
     *
     * @param transaction Transaction object to save
     */
    void save(Transaction transaction);

    /**
     * Returns an {@link Optional<Transaction>} instance given {@code id} argument.
     *
     * @param id Transaction's identifier
     * @return Optional transaction
     */
    Optional<Transaction> findById(String id);

    /**
     * Return a list of {@link Transaction} given {@code customerId} and {@code month} arguments.
     *
     * @param customerId Customer identifier
     * @param start      Start of the month
     * @param end        End of the month
     * @return List of monthly transactions found for a given customer
     */
    List<Transaction> findByCustomerIdAndDate(String customerId, Date start, Date end);

    /**
     * Return a list of {@link Transaction} given {@code customerId} argument.
     *
     * @param customerId Customer identifier
     * @return List of monthly transactions found for a given customer
     */
    List<Transaction> findByCustomerId(String customerId);

    /**
     * Returns a list of all {@link Transaction}.
     *
     * @return List of transactions found
     */
    List<Transaction> findAll();

    /**
     * Return a list of {@link Transaction} given {@link Pageable} argument.
     *
     * @param pageable Pageable argument
     * @return List of transactions found
     */
    List<Transaction> findAll(Pageable pageable);

    /**
     * Sets a {@link TransactionRepository} instance.
     *
     * @param repository Transaction repository instance
     */
    void setRepository(TransactionRepository repository);
}
