package com.ing.direct.transaction.dto;


import lombok.Data;

import java.math.BigDecimal;

/**
 * Transaction DTO class representing a customer transaction.
 */
@Data
public class TransactionDto {
    private String id;
    private String customer;
    private String date;
    private BigDecimal amount;
    private String description;
}


