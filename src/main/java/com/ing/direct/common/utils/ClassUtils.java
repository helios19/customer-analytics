package com.ing.direct.common.utils;

import com.google.common.primitives.Ints;
import com.ing.direct.transaction.dto.TransactionDto;
import com.ing.direct.transaction.exception.InvalidParameterException;
import com.ing.direct.transaction.exception.InvalidTransactionException;
import com.ing.direct.transaction.model.Transaction;
import org.apache.commons.lang3.tuple.Pair;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * Utils class providing convenient factory and helper methods for {@link Transaction} resources.
 */
public class ClassUtils {
    public static final String COUNTERS_COLLECTION_NAME = "counters";
    public static final String TRANSACTIONS_COLLECTION_NAME = "transactions";
    public static final String DATE_FORMAT_PATTERN = "yyyyMMdd";
    public static final DateTimeFormatter FORMATTER = ofPattern("d/MM/yyyy h:mm:ss a");
    public static final int DEFAULT_PAGE_SIZE = 50;
    public static final ModelMapper MODEL_MAPPER = new ModelMapper();

    private ClassUtils() {
    }

    /**
     * Validates transaction fields.
     *
     * @param transaction Transaction instance to validate
     * @throws InvalidTransactionException in case one of the transaction fields is invalid
     */
    public static void validateTransaction(Transaction transaction) {

        // validate transaction

        if (transaction == null || transaction.getCustomer() == null) {
            throw new InvalidTransactionException(transaction);
        }

    }

    /**
     * Validates transaction identifier field.
     *
     * @param customerId Transaction identifier
     * @return Transaction identifier if valid
     * @throws InvalidParameterException in case transaction identifier is invalid
     */
    public static String validateCustomerId(String customerId) {
        if (Ints.tryParse(customerId) == null) {
            throw new InvalidParameterException("customerId", customerId);
        }

        return customerId;
    }

    /**
     * Validates transaction identifier field.
     *
     * @param month Month
     * @return Month if valid
     * @throws InvalidParameterException in case month is invalid
     */
    public static int validateMonth(String month) {

        Integer iMonth = Ints.tryParse(month);

        if (iMonth == null || iMonth <= 0) {
            throw new InvalidParameterException("month", month);
        }

        return iMonth;
    }

    /**
     * Returns start and end date given month number.
     *
     * @param month Month
     * @return Pair of start and end date
     */
    public static Pair<Date, Date> toStartEndDate(int month) {
        LocalDateTime firstDayOfMonthDate = LocalDateTime.of(2016, month, 1, 0, 0, 0);
        LocalDateTime lastDayOfMonthDate = firstDayOfMonthDate.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1);

        return Pair.of(
                Date.from(firstDayOfMonthDate.atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(lastDayOfMonthDate.atZone(ZoneId.systemDefault()).toInstant()));
    }

    /**
     * Converts {@code isoDate} argument to {@link Date}.
     *
     * @param isoDate character sequence to convert
     * @return Date instance
     */
    public static Date toDate(String isoDate) {
        return Date.from(
                LocalDateTime.parse(isoDate, FORMATTER)
                        .atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    /**
     * Converts {@link Date} argument to {@code isoDate}.
     *
     * @param date Date to format
     * @return Date instance
     */
    public static String fromDate(Date date) {
        return FORMATTER.format(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));
    }

    /**
     * Converts a {@link Transaction} instance into a {@link TransactionDto} object.
     *
     * @param transaction Transaction to convert
     * @return TransactionDto
     */
    public static TransactionDto convertToDto(Transaction transaction) {
        TransactionDto transactionDto = MODEL_MAPPER.map(transaction, TransactionDto.class);
        transactionDto.setDate(fromDate(transaction.getDate()));
        return transactionDto;
    }

//    public static final void main(String... args) {
//
//        System.out.println("toDate : " + toDate("31/05/2016 4:20:23 PM"));
//        System.out.println("toDate : " + toDate("31/5/2016 9:15:24 AM"));
//        System.out.println("toDate : " + toDate("31/5/2016 9:15:24 PM"));
//    }
}
