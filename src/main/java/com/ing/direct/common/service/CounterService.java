package com.ing.direct.common.service;

/**
 * Counter service interface declaring a convenient method returning the next sequence integer given a collection name.
 *
 * @see CounterServiceImpl
 */
public interface CounterService {
    int getNextSequence(String collectionName);
}
