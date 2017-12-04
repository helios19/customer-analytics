package com.ing.direct.transaction.service;

import com.ing.direct.transaction.model.Transaction;

import java.util.List;

/**
 * Classification service interface.
 */
public interface ClassificationService {

    List<ClassificationEnum> classifyCustomer(List<Transaction> transactions);
}
