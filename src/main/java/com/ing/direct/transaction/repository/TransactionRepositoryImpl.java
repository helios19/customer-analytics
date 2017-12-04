package com.ing.direct.transaction.repository;

import com.google.common.base.Strings;
import com.ing.direct.common.service.CounterService;
import com.ing.direct.transaction.model.Transaction;
import com.mongodb.MongoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Repository implementation class methods to manipulate {@link Transaction} resource in database.
 * This class inherits from {@link TransactionRepositoryCustom}
 *
 * @see TransactionRepositoryCustom
 */
@Repository
public class TransactionRepositoryImpl implements TransactionRepositoryCustom {

    private static final Logger LOG = LoggerFactory.getLogger(com.ing.direct.transaction.repository.TransactionRepositoryImpl.class);

    private MongoTemplate mongoTemplate;

    private CounterService counterService;

    @Autowired
    public TransactionRepositoryImpl(MongoTemplate mongoTemplate, CounterService counterService) {
        this.mongoTemplate = mongoTemplate;
        this.counterService = counterService;
    }

    @Override
    public List<Transaction> findByCustomerIdAndDate(String customerId, Date start, Date end) {

        Query query = new Query(new Criteria().andOperator(
                where("customer").is(customerId),
                where("date").gte(start),
                where("date").lt(end)));
        query.with(new Sort(Sort.Direction.DESC, "date"));

        return mongoTemplate.find(query, Transaction.class);

    }

    @Override
    public List<Transaction> findByCustomerId(String customerId) {

        Query query = new Query(new Criteria().where("customer").is(customerId));
        query.with(new Sort(Sort.Direction.DESC, "date"));

        return mongoTemplate.find(query, Transaction.class);

    }

    @Override
    public void saveOrUpdate(Transaction... transactions) {

        Arrays.asList(transactions).stream().forEach(transaction -> {
            Criteria criteria = getCriteria(transaction);

            Update update = getUpdate(transaction);

            Query query = new Query(criteria);

            try {
                // add an identifier only for new entry
                if (!mongoTemplate.exists(query, Transaction.class)) {
                    update.set("id", new Integer(counterService.getNextSequence("transactions")).toString());
                }

                // insert or update transaction
                mongoTemplate.upsert(query, update, Transaction.class);
            } catch (MongoException me) {
                LOG.error("An error occurred while upserting transaction[{},{}] ",
                        transaction.getCustomer(), transaction.getDate(), me);
            }
        });

    }

    private Update getUpdate(Transaction transaction) {
        return new Update()
//                .set("id", new Integer(counterService.getNextSequence("transactions")).toString())
                .set("customer", transaction.getCustomer())
                .set("date", transaction.getDate())
                .set("amount", transaction.getAmount())
                .set("description", transaction.getDescription());
    }

    private Criteria getCriteria(Transaction transaction) {

        Criteria criteria;

        if (!Strings.isNullOrEmpty(transaction.getId())) {
            criteria = where("id").is(transaction.getId());
        } else {
            criteria = new Criteria()
                    .andOperator(
                            where("customer").is(transaction.getCustomer()),
                            where("date").is(transaction.getDate()),
                            where("amount").is(transaction.getAmount()),
                            where("description").is(transaction.getDescription()));
        }

        return criteria;
    }

    @Override
    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    @Override
    public void setMongoTemplate(MongoTemplate template) {
        this.mongoTemplate = template;
    }

}