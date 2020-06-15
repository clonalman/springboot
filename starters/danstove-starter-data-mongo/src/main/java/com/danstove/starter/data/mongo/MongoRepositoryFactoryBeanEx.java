package com.danstove.starter.data.mongo;

import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.support.MappingMongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import java.io.Serializable;

public class MongoRepositoryFactoryBeanEx<T extends MongoRepository<S, ID>, S, ID extends Serializable>
        extends MongoRepositoryFactoryBean<T, S, ID> {

    public MongoRepositoryFactoryBeanEx(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport getFactoryInstance(MongoOperations operations) {
        return new MongoRepositoryFactoryImpl(operations);
    }

    private static class MongoRepositoryFactoryImpl<S, ID extends Serializable> extends MongoRepositoryFactory {

        private final MongoOperations operations;
        private final MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext;


        public MongoRepositoryFactoryImpl(MongoOperations mongoOperations) {
            super(mongoOperations);
            this.operations = mongoOperations;
            this.mappingContext = mongoOperations.getConverter().getMappingContext();
    }

        @Override
        protected Object getTargetRepository(RepositoryInformation information) {
            Class<?> repositoryInterface = information.getRepositoryInterface();
            if(repositoryInterface != null) {
                Document document = repositoryInterface.getAnnotation(Document.class);
                if(document != null) {
                    MongoPersistentEntity<?> entity = this.mappingContext.getRequiredPersistentEntity(information.getDomainType());
                    if(entity != null) {
                        MappingMongoEntityInformation entityInformation = new MappingMongoEntityInformation(entity, document.value());
                        return this.getTargetRepositoryViaReflection(information, new Object[]{entityInformation, this.operations});
                    }
                }
            }
            return super.getTargetRepository(information);
        }
    }
}