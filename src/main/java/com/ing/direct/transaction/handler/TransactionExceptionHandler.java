package com.ing.direct.transaction.handler;

import com.ing.direct.transaction.exception.TransactionNotFoundException;
import com.ing.direct.transaction.exception.InvalidTransactionException;
import com.ing.direct.transaction.exception.InvalidParameterException;
import com.ing.direct.common.handler.GlobalExceptionHandler;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception handler class used for catching any {@link com.ing.direct.transaction.model.Transaction} related exceptions
 * and transforming them into HATEOAS JSON message.
 *
 * @see VndErrors
 * @see com.ing.direct.transaction.model.Transaction
 * @see InvalidTransactionException
 * @see InvalidParameterException
 * @see TransactionNotFoundException
 */
@ControllerAdvice
public class TransactionExceptionHandler extends GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(InvalidTransactionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    VndErrors handleInvalidTransactionException(InvalidTransactionException ex) {
        return getVndErrors(ex);
    }

    @ResponseBody
    @ExceptionHandler(InvalidParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    VndErrors handleInvalidParameterException(InvalidParameterException ex) {
        return getVndErrors(ex);
    }

    @ResponseBody
    @ExceptionHandler(TransactionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    VndErrors handleTransactionNotFoundException(TransactionNotFoundException ex) {
        return getVndErrors(ex);
    }

}
