package com.danstove.starter.data.mongo;

import com.mongodb.client.result.UpdateResult;
import org.bson.BsonValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.data.mongodb.util.BsonUtils;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

/**
 * @author: djgao.gaudi
 * @Date: 2020/01/01
 */
@NoRepositoryBean
public class MongoRepositoryImpl<T, ID> extends SimpleMongoRepository<T, ID> implements MongoQueryExecutor<T> {

    private final static Logger LOG = LoggerFactory.getLogger(MongoRepositoryImpl.class);

    private final MongoOperations mongoOperations;
    private final MongoEntityInformation<T, ID> entityInformation;

    public MongoRepositoryImpl(MongoEntityInformation<T, ID> metadata, MongoOperations mongoOperations) {
        super(metadata, mongoOperations);
        this.entityInformation = metadata;
        this.mongoOperations = mongoOperations;
    }
    /**
     * 根据条件查询集合
     *
     * @param query
     * @return
     */
    public List<T> findAll(MongoQuery query) {
        return mongoOperations.find(query.createQuery(), entityInformation.getJavaType(), entityInformation.getCollectionName());
    }

    /**
     * 根据条件查询只返回一个文档
     *
     * @param query
     * @return
     */
    public Optional<T> findOne(MongoQuery query) {
        return Optional.ofNullable(mongoOperations.findOne(query.createQuery(), entityInformation.getJavaType(), entityInformation.getCollectionName()));
    }

    /***
     * 根据条件分页查询
     * @param query
     * @return
     */
    public Page<T> findAll(MongoQuery query, Pageable pageable) {
        List<T> content = this.mongoOperations.find(query.createQuery().with(pageable), entityInformation.getJavaType(), entityInformation.getCollectionName());
        long total = this.mongoOperations.count(query.createQuery(), entityInformation.getJavaType(), entityInformation.getCollectionName());
        return new PageImpl(content, pageable, total);
    }

    /***
     * 根据条件查询库中符合条件的记录数量
     * @param query
     * @return
     */
    public long count(MongoQuery query) {
        return this.mongoOperations.count(query.createQuery(), entityInformation.getJavaType(), entityInformation.getCollectionName());
    }

    /**
     * 修改匹配到的第一条记录
     * @param query
     * @param properties
     */
    public long updateFirst(MongoQuery query, Object... properties){
        return mongoOperations.updateFirst(
                query.createQuery(),
                query.createUpdate(properties),
                entityInformation.getJavaType(),
                entityInformation.getCollectionName()).getModifiedCount();
    }

    /***
     * 修改匹配到的所有记录
     * @param query
     * @param properties
     */
    public long updateMulti(MongoQuery query, Object... properties){
        return mongoOperations.updateMulti(
                query.createQuery(),
                query.createUpdate(properties),
                entityInformation.getJavaType(),
                entityInformation.getCollectionName()).getModifiedCount();
    }

    /***
     * 修改匹配到的记录，若不存在该记录则进行添加
     * @param query
     * @param properties
     */
    public Object upsert(MongoQuery query, Object... properties){
        UpdateResult result = mongoOperations.upsert(
                query.createQuery(),
                query.createUpdate(properties),
                entityInformation.getJavaType(),
                entityInformation.getCollectionName());
        BsonValue bsonValue = result.getUpsertedId();
        if(bsonValue != null){
            return (ID)BsonUtils.toJavaType(bsonValue);
        }
        return null;
    }
}