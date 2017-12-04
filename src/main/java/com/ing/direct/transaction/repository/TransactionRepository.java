package com.ing.direct.transaction.repository;

import com.ing.direct.transaction.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String>, TransactionRepositoryCustom {

    Optional<Transaction> findById(String id);

}

