package com.danstove.starter.data.mongo;

/**
 * Created by djgao.Gaudi1 on 2018/5/18.
 */
public class MongoKey implements java.io.Serializable {
    /**
     * The constant DEFAULT.
     */
    public static final MongoKey DEFAULT = new MongoKey("default", false);
    /**
     * key
     */
    private String key;
    /**
     * active
     */
    private boolean active;

    /**
     * Instantiates a new Mongo key.
     *
     * @param key the key
     */
    public MongoKey(String key) {
        this(key, false);
    }

    /**
     * Instantiates a new Mongo key.
     *
     * @param key    the key
     * @param active the active
     */
    public MongoKey(String key, boolean active) {
        this.key = key;
        this.active = active;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || (obj instanceof MongoKey)) {
            return false;
        }

        return key.equals(((MongoKey) obj).getKey());
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    /**
     * Is active boolean.
     *
     * @return the boolean
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets active.
     *
     * @param active the active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Is default boolean.
     *
     * @return the boolean
     */
    public boolean isDefault() {
        return this.equals(DEFAULT);
    }

    /**
     * Gets key.
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets key.
     *
     * @param key the key
     */
    public void setKey(String key) {
        this.key = key;
    }
}