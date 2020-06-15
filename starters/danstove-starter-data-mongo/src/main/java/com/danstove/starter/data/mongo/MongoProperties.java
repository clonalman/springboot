package com.danstove.starter.data.mongo;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * Created by djgao on 2017/9/29.
 */
@ConfigurationProperties("spring.data.mongodb")
public class MongoProperties implements java.io.Serializable {

    /**
     * The uri.
     */
    private String uri;

    /**
     * 客户端连接
     */
    private Map<String, String> clients;

    /**
     * Gets the uri.
     *
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * Sets the uri.
     *
     * @param uri the new uri
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * Gets clients.
     *
     * @return Map<String String>
     */
    public Map<String, String> getClients() {
        return clients;
    }

    /**
     * Sets clients.
     *
     * @param clients the clients
     */
    public void setClients(Map<String, String> clients) {
        this.clients = clients;
    }
}
