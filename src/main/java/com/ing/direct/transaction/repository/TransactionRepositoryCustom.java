package com.ing.direct.transaction.repository;

import com.ing.direct.transaction.model.Transaction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepositoryCustom {

    void saveOrUpdate(Transaction... transaction);

    List<Transaction> findByCustomerIdAndDate(String customerId, Date start, Date end);

    List<Transaction> findByCustomerId(String customerId);

    MongoTemplate getMongoTemplate();

    void setMongoTemplate(MongoTemplate template);

}
