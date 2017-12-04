package com.ing.direct.transaction.service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.ing.direct.transaction.model.Transaction;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class implementing logic to determine a customer classification from a input list of transactions.
 *
 * @see ClassificationService
 */
@Service
public class ClassificationServiceImpl implements ClassificationService {
    private static final LocalTime MIDDAY = LocalTime.of(12, 0);

    /**
     * Returns the customer classification given {@code month} and {@code transactions} arguments.
     *
     * @param transactions List of transactions
     * @return List of customer classifications
     */
    @Override
    public List<ClassificationEnum> classifyCustomer(List<Transaction> transactions) {

        if (CollectionUtils.isEmpty(transactions)) {
            return Lists.newArrayList(ClassificationEnum.UNKNOWN);
        }

        List<ClassificationEnum> customerClassifications = Lists.newArrayList();

        runClassificationRules(customerClassifications, transactions);

        return CollectionUtils.isEmpty(customerClassifications)
                ? ImmutableList.of(ClassificationEnum.UNKNOWN)
                : ImmutableList.copyOf(customerClassifications);
    }

    /**
     * Runs classification rules.
     *
     * @param customerClassifications Customer classification matrix
     * @param monthlyTransactions     List of monthly transactions
     */
    private void runClassificationRules(List<ClassificationEnum> customerClassifications, List<Transaction> monthlyTransactions) {

        afternoonPersonRule(monthlyTransactions, customerClassifications);

        bigSpenderRule(monthlyTransactions, customerClassifications);

        bigTicketSpenderRule(monthlyTransactions, customerClassifications);

        fastSpenderRule(monthlyTransactions, customerClassifications);

        morningPersonRule(monthlyTransactions, customerClassifications);

        potentialSaverRule(monthlyTransactions, customerClassifications);

        potentialLoanRule(monthlyTransactions, customerClassifications);
    }

    /**
     * Determines if customer makes over 50% of their transactions in the month after midday (count of transactions).
     *
     * @param transactions List of transactions
     * @return If customer is classified as AFTERNOON_PERSON
     */
    private void afternoonPersonRule(List<Transaction> transactions, List<ClassificationEnum> customerClassifications) {

        long totalTransactions = transactions.size();

        long totalAfternoonTransactions = transactions
                .stream()
                .filter(t -> t.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalTime().isAfter(MIDDAY))
                .count();

        if (totalAfternoonTransactions > (totalTransactions / 2)) {
            customerClassifications.add(ClassificationEnum.AFTERNOON_PERSON);
        }
    }

    /**
     * Determines if customer spends over 80% of their deposits every month ($ value of deposits).
     *
     * @param transactions List of transactions
     * @return If customer is classified as BIG_SPENDER
     */
    private void bigSpenderRule(List<Transaction> transactions, List<ClassificationEnum> customerClassifications) {

        BigDecimal deposits = BigDecimal.valueOf(transactions
                .stream()
                .filter(t -> t.getAmount().doubleValue() > 0)
                .mapToDouble(t -> t.getAmount().doubleValue()).sum());

        if (deposits.doubleValue() == 0) {
            return;
        }

        BigDecimal expenditures = BigDecimal.valueOf(transactions
                .stream()
                .filter(t -> t.getAmount().doubleValue() < 0)
                .mapToDouble(t -> t.getAmount().abs().doubleValue()).sum());


        if (expenditures.doubleValue() > deposits.multiply(BigDecimal.valueOf(0.8)).doubleValue()) {
            customerClassifications.add(ClassificationEnum.BIG_SPENDER);
        }
    }

    /**
     * Determines if customer makes one or more withdrawals over $1000 in the month.
     *
     * @param transactions List of transactions
     * @return If customer is classified as BIG_TICKET_SPENDER
     */
    private void bigTicketSpenderRule(List<Transaction> transactions, List<ClassificationEnum> customerClassifications) {

        if (transactions
                .stream()
                .filter(t -> t.getAmount().doubleValue() < -1000)
                .findFirst().isPresent()) {
            customerClassifications.add(ClassificationEnum.BIG_TICKET_SPENDER);
        }
    }

    /**
     * Determines if customer spends over 75% of any deposit within 7 days of making it.
     *
     * @param transactions List of transactions
     * @return If customer is classified as FAST_SPENDER
     */
    private void fastSpenderRule(List<Transaction> transactions, List<ClassificationEnum> customerClassifications) {

        List<Transaction> deposits = transactions
                .stream()
                .filter(t -> t.getAmount().doubleValue() > 0)
                .collect(Collectors.toList());

        for (Transaction depositTransaction : deposits) {

            Instant depositDate = depositTransaction.getDate().toInstant();
            BigDecimal depositAmount = depositTransaction.getAmount();
            Instant sevenDaysAfterDepositDate = depositDate.atZone(ZoneId.systemDefault()).plusDays(7).toInstant();

            BigDecimal expenditures = BigDecimal.valueOf(transactions
                    .stream()
                    .filter(t -> t.getAmount().doubleValue() < 0
                            && t.getDate().toInstant().isAfter(depositDate)
                            && t.getDate().toInstant().isBefore(sevenDaysAfterDepositDate))
                    .mapToDouble(t -> t.getAmount().abs().doubleValue()).sum());

            if (expenditures.doubleValue() > depositAmount.multiply(BigDecimal.valueOf(0.75)).doubleValue()) {
                customerClassifications.add(ClassificationEnum.FAST_SPENDER);
                return;
            }
        }
    }

    /**
     * Determines if customer makes over 50% of their transactions in the month before midday (count of transactions).
     *
     * @param transactions List of transactions
     * @return If customer is classified as MORNING_PERSON
     */
    private void morningPersonRule(List<Transaction> transactions, List<ClassificationEnum> customerClassifications) {

        long totalTransactions = transactions.size();

        long totalMorningTransactions = transactions
                .stream()
                .filter(t -> t.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalTime().isBefore(MIDDAY))
                .count();

        if (totalMorningTransactions > (totalTransactions / 2)) {
            customerClassifications.add(ClassificationEnum.MORNING_PERSON);
        }
    }

    /**
     * Determines if customer spends less than 25% of their deposits every month ($ value of deposits).
     *
     * @param transactions List of transactions
     * @return If customer is classified as POTENTIAL_SAVER
     */
    private void potentialSaverRule(List<Transaction> transactions, List<ClassificationEnum> customerClassifications) {

        BigDecimal deposits = BigDecimal.valueOf(transactions
                .stream()
                .filter(t -> t.getAmount().doubleValue() > 0)
                .mapToDouble(t -> t.getAmount().doubleValue()).sum());

        if (deposits.doubleValue() == 0) {
            return;
        }

        BigDecimal expenditures = BigDecimal.valueOf(transactions
                .stream()
                .filter(t -> t.getAmount().doubleValue() < 0)
                .mapToDouble(t -> t.getAmount().abs().doubleValue()).sum());


        if (expenditures.doubleValue() < deposits.multiply(BigDecimal.valueOf(0.25)).doubleValue()) {
            customerClassifications.add(ClassificationEnum.POTENTIAL_SAVER);
        }
    }

    /**
     * Determines if a customer is identified as both a Big Spender and a Fast Spender then they should be classified as
     * a Potential Loan.
     *
     * @param transactions List of transactions
     * @return If customer is classified as POTENTIAL_LOAN
     */
    private void potentialLoanRule(List<Transaction> transactions, List<ClassificationEnum> customerClassifications) {
        List classificationBigAndFastSpender = Lists.newArrayList(ClassificationEnum.BIG_SPENDER,
                ClassificationEnum.FAST_SPENDER);

        if (customerClassifications.containsAll(classificationBigAndFastSpender)) {
            customerClassifications.removeAll(classificationBigAndFastSpender);
            customerClassifications.add(ClassificationEnum.POTENTIAL_LOAN);
        }
    }
}