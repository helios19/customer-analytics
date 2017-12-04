package com.ing.direct.transaction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an input parameter is invalid.
 *
 * @see HttpStatus
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidParameterException extends RuntimeException {
    public InvalidParameterException(String paramName, String paramValue) {
        super("Invalid parameter value [param:" + paramName + ", value:" + paramValue + "]");
    }
}
