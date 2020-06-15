package com.danstove.starter.data.mongo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MongoQueryExecutor<T> {
    List<T> findAll(MongoQuery query);
    Optional<T> findOne(MongoQuery query);
    Page<T> findAll(MongoQuery query, Pageable pageable);
    long count(MongoQuery query);
    long updateFirst(MongoQuery query, Object... properties);
    long updateMulti(MongoQuery query, Object... properties);
    Object upsert(MongoQuery query, Object... properties);
}
