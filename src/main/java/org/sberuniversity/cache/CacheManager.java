package org.sberuniversity.cache;

public interface CacheManager {

    boolean contains(Object[] args);

    void put(Object[] args, Object value);

    Object get(Object[] args);

}
