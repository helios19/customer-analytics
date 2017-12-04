package com.ing.direct.transaction.service;

import lombok.Getter;

/**
 * Classification enum grouping customer behavior into classification categories.
 */
@Getter
public enum ClassificationEnum {
    AFTERNOON_PERSON("Makes over 50% of their transactions in the month after midday (count of transactions)"),
    BIG_SPENDER("Spends over 80% of their deposits every month ($ value of deposits)"),
    BIG_TICKET_SPENDER("Makes one or more withdrawals over $1000 in the month"),
    FAST_SPENDER("Spends over 75% of any deposit within 7 days of making it"),
    MORNING_PERSON("Makes over 50% of their transactions in the month before midday (count of transactions)"),
    POTENTIAL_SAVER("Spends less than 25% of their deposits every month ($ value of deposits)"),
    POTENTIAL_LOAN("Identified as both a Big Spender and a Fast Spender"),
    UNKNOWN("N/A");

    private String description;

    ClassificationEnum(String description) {
        this.description = description;
    }
}
