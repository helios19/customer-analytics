package com.ing.direct.transaction.exception;

import com.ing.direct.transaction.model.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an {@link com.ing.direct.transaction.model.Transaction} instance contains invalid field values
 * (e.g {@link com.ing.direct.transaction.model.Transaction#customer} is null, invalid amount, etc.)
 *
 * @see com.ing.direct.transaction.model.Transaction
 * @see HttpStatus
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTransactionException extends RuntimeException {
    public InvalidTransactionException(Transaction transaction) {
        super("Invalid Transaction field values [" + transaction + "]");
    }
}
