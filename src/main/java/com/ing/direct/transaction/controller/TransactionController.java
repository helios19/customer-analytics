package com.ing.direct.transaction.controller;

import com.ing.direct.transaction.dto.TransactionDto;
import com.ing.direct.transaction.dto.TransactionSummary;
import com.ing.direct.transaction.exception.TransactionNotFoundException;
import com.ing.direct.transaction.model.Transaction;
import com.ing.direct.transaction.service.ClassificationEnum;
import com.ing.direct.transaction.service.ClassificationService;
import com.ing.direct.transaction.service.TransactionService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

import static com.ing.direct.common.utils.ClassUtils.*;

/**
 * Transaction controller class defining the HTTP operations available for the {@link Transaction} resource. This controller
 * is mainly used to return a customer transaction summary including its classification.
 *
 * @see Transaction
 * @see TransactionSummary
 * @see ClassificationEnum
 * @see RestController
 */
@RestController
@RequestMapping("/transaction-summary")
public class TransactionController {

    private final TransactionService transactionService;

    private final ClassificationService classificationService;

    @Autowired
    public TransactionController(TransactionService transactionService, ClassificationService classificationService) {
        this.transactionService = transactionService;
        this.classificationService = classificationService;
    }

    /**
     * Returns the transaction summary of a given {@code customerId} and {@code month} parameters.
     *
     * @param customerId Customer identifier
     * @param month      Month
     * @return Transaction summary for the give customer
     * @throws TransactionNotFoundException if no transaction for the given customerId and month can be found
     */
    @RequestMapping(value = "/{customerId}/{month}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<TransactionSummary> findByCustomerIdMonth(@PathVariable("customerId") String customerId, @PathVariable("month") String month) {

        List<Transaction> transactions = transactionService.findByCustomerId(validateCustomerId(customerId));
        List<Transaction> monthlyTransactions = getMonthlyTransactions(transactions, validateMonth(month));

        if (CollectionUtils.isEmpty(transactions) || CollectionUtils.isEmpty(monthlyTransactions)) {
            throw new TransactionNotFoundException(customerId);
        }

        TransactionSummary transactionSummary = TransactionSummary
                .builder()
                .customerId(customerId)
                .month(month)
                .currentBalance(getCurrentBalance(transactions))
                .classification(getClassification(monthlyTransactions))
                .transactions(convertToTransactionDtos(monthlyTransactions))
                .build();

        return ResponseEntity
                .ok()
                .body(transactionSummary);
    }

    /**
     * Converts a list of transactions to its Dto representation.
     *
     * @param transactions List of transactions to convert
     * @return List of TransactionDtos
     */
    private List<TransactionDto> convertToTransactionDtos(List<Transaction> transactions) {
        return transactions.stream()
                .map(transaction -> convertToDto(transaction))
                .collect(Collectors.toList());
    }

    /**
     * Returns the classification label according to the list of transactions passed as argument.
     *
     * @param transactions List of transactions
     * @return Customer classification label
     */
    private List<ClassificationEnum> getClassification(List<Transaction> transactions) {
        return classificationService.classifyCustomer(transactions);
    }

    /**
     * Returns the total balance given the list of transactions passed as argument.
     *
     * @param transactions List of transactions
     * @return Total balance
     */
    private double getCurrentBalance(List<Transaction> transactions) {
        return BigDecimal.valueOf(transactions.stream().mapToDouble(t -> t.getAmount().doubleValue()).sum())
                .setScale(2, RoundingMode.CEILING)
                .doubleValue();
    }

    /**
     * Returns the related monthly transactions from an input list of transactions.
     *
     * @param transactions List of transactions
     * @param month        Month filter
     * @return Monthly transactions
     */
    private List<Transaction> getMonthlyTransactions(List<Transaction> transactions, int month) {

        LocalDateTime firstDayOfMonthDate = LocalDateTime.of(2016, month, 1, 0, 0, 0);
        LocalDateTime lastDayOfMonthDate = firstDayOfMonthDate.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1);

        return transactions
                .stream()
                .filter(t -> t.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().isAfter(firstDayOfMonthDate)
                        && t.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().isBefore(lastDayOfMonthDate))
                .collect(Collectors.toList());

    }

}
