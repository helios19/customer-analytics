package com.ing.direct.transaction.dto;

import com.ing.direct.transaction.service.ClassificationEnum;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Transaction Summary DTO class gathering customer transaction details including {@link #customerId}, {@link #month}
 * {@link #classification} and {@link #transactions}.
 *
 * @see TransactionDto
 * @see ClassificationEnum
 */
@Data
@Builder
public class TransactionSummary {
    private String customerId;
    private String month;
    private double currentBalance;
    private List<ClassificationEnum> classification;
    private List<TransactionDto> transactions;
}
