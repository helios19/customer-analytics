package com.ing.direct.common.service;

import com.ing.direct.common.sequence.Counter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import static com.ing.direct.common.utils.ClassUtils.COUNTERS_COLLECTION_NAME;
import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Counter service class providing a convenient {@link #getNextSequence(String)} sequence method
 * in an attempt to replicate * the behavior of the {@code GeneratedValue} and {@code SequenceGenerator}
 * annotations supported by most of JPA databases but missing in Mongodb.
 * <p>
 * <p>Note that a separate {@link com.ing.direct.common.utils.ClassUtils#COUNTERS_COLLECTION_NAME}
 * collection is used to hold the document sequences.</p>
 */
@Service
public class CounterServiceImpl implements CounterService {

    @Autowired
    private MongoOperations mongo;

    /**
     * Returns the next seauence integer for a given {@code collectionName}.
     *
     * @param collectionName Collection name
     * @return Next sequence integer
     */
    public int getNextSequence(String collectionName) {

        if (!mongo.collectionExists(COUNTERS_COLLECTION_NAME)) {
            //db.Counter.insert({ 'name' : 'user_id', sequence : 1}
            mongo.insert(Counter.builder().id(collectionName).seq(0).build(), COUNTERS_COLLECTION_NAME);
        }

        Counter counter = mongo.findAndModify(
                query(where("_id").is(collectionName)),
                new Update().inc("seq", 1),
                options().returnNew(true),
                Counter.class);

        return counter.getSeq();
    }
}