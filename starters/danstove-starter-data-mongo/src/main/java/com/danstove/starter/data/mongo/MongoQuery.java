package com.danstove.starter.data.mongo;

import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.apache.commons.collections.MapUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by djgao.Gaudi1 on 2020/4/1.
 */
public final class MongoQuery implements java.io.Serializable {

    private final static Logger LOG = LoggerFactory.getLogger(MongoQuery.class);

    private QueryBuilder queryBuilder = new QueryBuilder();

    public Query createQuery() {
        return new BasicQuery(queryBuilder.get().toString());
    }

    public Update createUpdate(Object... properties) {
        Update update = new Update();
        Map<String, Object> data = new HashMap<>();
        MapUtils.putAll(data, properties);
        for (String fieldName : data.keySet()) {
            update = update.set(fieldName, data.get(fieldName));
        }
        return update;
    }

    public static MongoQuery start() {
        return new MongoQuery();
    }

    public static MongoQuery start(String key) {
        return new MongoQuery().start().put(key);
    }

    public MongoQuery put(String key) {
        queryBuilder.put(key);
        return this;
    }

    public MongoQuery and(MongoQuery... qs){
        if(qs.length > 0) {
            List<DBObject> ands = new ArrayList<>();
            for (MongoQuery query : qs) {
                ands.add(query.queryBuilder.get());
            }
            queryBuilder.and(ands.toArray(new DBObject[ands.size()]));
        }
        return this;
    }

    public MongoQuery or(MongoQuery... qs){
        if(qs.length > 0) {
            List<DBObject> ors = new ArrayList<>();
            for (MongoQuery query : qs) {
                ors.add(query.queryBuilder.get());
            }
            queryBuilder.or(ors.toArray(new DBObject[ors.size()]));
        }
        return this;
    }

    public MongoQuery nor(MongoQuery... qs){
        if(qs.length > 0) {
            List<DBObject> ors = new ArrayList<>();
            for (MongoQuery query : qs) {
                ors.add(query.queryBuilder.get());
            }
            queryBuilder.not().or(ors.toArray(new DBObject[ors.size()]));
        }
        return this;
    }

    public MongoQuery eq(String key, Object value) {
        queryBuilder.and(key).is(value);
        return this;
    }

    public MongoQuery ne(String key, Object value) {
        queryBuilder.and(key).notEquals(value);
        return this;
    }

    public MongoQuery regex(String key, String regex) {
        queryBuilder.and(key).regex(Pattern.compile(regex));
        return this;
    }

    public MongoQuery gt(String key, Object value) {
        queryBuilder.and(key).greaterThan(value);
        return this;
    }

    public MongoQuery lt(String key, Object value) {
        queryBuilder.and(key).lessThan(value);
        return this;
    }

    public MongoQuery gte(String key, Object value) {
        queryBuilder.and(key).greaterThanEquals(value);
        return this;
    }

    public MongoQuery lte(String key, Object value) {
        queryBuilder.and(key).lessThanEquals(value);
        return this;
    }

    public MongoQuery mod(String key, long divisor, long remainder) {
        queryBuilder.and(key).mod(Arrays.asList(divisor, remainder));
        return this;
    }

    public MongoQuery elemMatch(String key, MongoQuery... qs) {
        queryBuilder.and(key).elemMatch(and(qs).queryBuilder.get());
        return this;
    }

    public MongoQuery in(String key, List values) {
        if ("_id".equals(key)) {
            List<ObjectId> idValues = new ArrayList<>();
            Iterator iter = values.iterator();
            while (iter.hasNext()) {
                Object value = iter.next();
                idValues.add(new ObjectId(String.valueOf(value)));
            }
            queryBuilder.in(idValues);
        } else {
            queryBuilder.and(key).in(values);
        }
        return this;
    }

    public MongoQuery size(String key, int size) {
        queryBuilder.and(key).size(size);
        return this;
    }

    public MongoQuery like(String key, String value) {
        queryBuilder.and(key).regex(Pattern.compile(value, Pattern.CASE_INSENSITIVE));
        return this;
    }

    public MongoQuery startsWith(String key, String value) {
        return like(key, "^" + value + ".*$");
    }

    public MongoQuery endsWith(String key, String value) {
        return like(key, "^.*" + value + "$");
    }

    public MongoQuery text(String search) {
        queryBuilder.text(search);
        return this;
    }

    public MongoQuery text(String search, String language) {
        queryBuilder.text(search, language);
        return this;
    }
}
