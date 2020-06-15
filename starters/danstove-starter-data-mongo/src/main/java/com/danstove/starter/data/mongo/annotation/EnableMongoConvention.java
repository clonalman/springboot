package com.danstove.starter.data.mongo.annotation;

import com.danstove.starter.data.mongo.MongoRepositoryFactoryBeanEx;
import com.danstove.starter.data.mongo.MongoRepositoryImpl;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Inherited
@EnableMongoRepositories(
        repositoryFactoryBeanClass= MongoRepositoryFactoryBeanEx.class,
        repositoryBaseClass = MongoRepositoryImpl.class,
        basePackages = { "com" },
        includeFilters = @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = ".*\\.repository\\.mongo\\.[^\\.]*"
        )
)
public @interface EnableMongoConvention {
}