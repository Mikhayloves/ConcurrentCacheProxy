package org.sberuniversity.cache;

import org.sberuniversity.proxy.ArgsKey;

public interface CacheManager {

    boolean contains(Object[] args);

    void put(Object[] args, Object value);

    Object get(Object[] args);

    ArgsKey getValuableArgs(Object[] args);

}
