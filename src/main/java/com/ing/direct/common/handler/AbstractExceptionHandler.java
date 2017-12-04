package com.ing.direct.common.handler;

import org.springframework.hateoas.VndErrors;

/**
 * Abstract handler class declaring common handling methods.
 */
public abstract class AbstractExceptionHandler {

    /**
     * Returns a {@link VndErrors} instance given a {@link RuntimeException} given as argument.
     *
     * @param ex RuntimeException to convert
     * @return VndErrors object
     */
    protected VndErrors getVndErrors(Exception ex) {
        return new VndErrors("error", ex.getMessage());
    }
}
