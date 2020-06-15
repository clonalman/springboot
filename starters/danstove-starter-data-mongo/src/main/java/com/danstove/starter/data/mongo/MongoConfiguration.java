package com.danstove.starter.data.mongo;

import com.mongodb.MongoClientURI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by djgao on 2017/9/29.
 * 约定mongo下的Repository使用此数据源
 */
@Configuration
@EnableConfigurationProperties(MongoProperties.class)
@ComponentScan("com.danstove.starter.data.mongo")
public class MongoConfiguration {
    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(MongoConfiguration.class);

    /**
     * The Constant MONGO_TEMPLATE.
     */
    static final String MONGO_TEMPLATE = "mongoTemplate";

    /**
     * mongoTemplates
     */
    private Map<MongoKey, MongoTemplate> mongoTemplates = new ConcurrentHashMap<>();

    /**
     * Mongo template.
     *
     * @param mongoClients the mongo
     * @return the mongo template
     * @throws UnknownHostException the unknown host exception
     */
    @Bean
    @Qualifier(MONGO_TEMPLATE)
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MongoTemplate mongoTemplate(@Qualifier("mongoClients") Map<MongoKey, MongoClientURI> mongoClients)
            throws UnknownHostException {

        for (MongoKey mongoKey : mongoClients.keySet()) {
            if (mongoKey.isActive()) {
                MongoClientURI clientURI = mongoClients.get(mongoKey);
                if(clientURI != null) {
                    logger.info("USE DYNAMIC TEMPLATE: " + mongoKey.getKey() + " = " + clientURI.getURI());
                    if (!mongoTemplates.containsKey(mongoKey)) {
                        mongoTemplates.put(mongoKey, new MongoTemplate(new SimpleMongoDbFactory(clientURI)));
                    }
                    return mongoTemplates.get(mongoKey);
                }
                //return new MongoTemplate(new SimpleMongoDbFactory(mongoClients.get(mongoKey)));
            }
        }

        MongoClientURI clientURI = mongoClients.get(MongoKey.DEFAULT);
        if(clientURI != null) {
            logger.info("USE DEFAULT TEMPLATE: " + MongoKey.DEFAULT.getKey() + " = " + clientURI.getURI());
            if (!mongoTemplates.containsKey(MongoKey.DEFAULT)) {
                mongoTemplates.put(MongoKey.DEFAULT, new MongoTemplate(new SimpleMongoDbFactory(mongoClients.get(MongoKey.DEFAULT))));
            }
        }
        return mongoTemplates.get(MongoKey.DEFAULT);
        //return new MongoTemplate(new SimpleMongoDbFactory(mongoClients.get(MongoKey.DEFAULT)));
    }

    /**
     * Mongo templates map.
     *
     * @param properties the properties
     * @return map
     * @throws UnknownHostException the unknown host exception
     */
    @Bean
    @Qualifier("mongoClients")
    public Map<MongoKey, MongoClientURI> mongoClients(MongoProperties properties) throws UnknownHostException {
        Map<MongoKey, MongoClientURI> templates = new HashMap<>();
        Map<String, String> clients = properties.getClients();

        if (clients != null) {
            for (String key : clients.keySet()) {
                Object value = clients.get(key);

                if (value != null) {
                    templates.put(new MongoKey(key), new MongoClientURI(String.valueOf(value)));
                }
            }
        }

        templates.put(MongoKey.DEFAULT, new MongoClientURI(properties.getUri()));

        return templates;
    }
}
