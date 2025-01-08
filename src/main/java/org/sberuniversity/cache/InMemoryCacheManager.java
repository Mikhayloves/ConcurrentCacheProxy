package org.sberuniversity.cache;

import org.sberuniversity.annotation.CacheConsts;
import org.sberuniversity.proxy.ArgsKey;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCacheManager implements CacheManager {
    protected HashMap<ArgsKey, Object> argsResultMap;
    private int valuableArgsCount;
    private Map<ArgsKey, ArgsKey> valuableArgsMap;


    public InMemoryCacheManager(int valuableArgsCount) {
        this.valuableArgsCount = valuableArgsCount;
        argsResultMap = new HashMap<>();
        valuableArgsMap = Collections.synchronizedMap(new HashMap<>());
    }

    public ArgsKey getValuableArgs(Object[] args) {
        ArgsKey key = calcValuableArgs(args);
        if (valuableArgsMap.containsKey(key)) {
            return valuableArgsMap.get(key);
        }
        valuableArgsMap.put(key, calcValuableArgs(args));
        return valuableArgsMap.get(key);
    }

    private synchronized ArgsKey calcValuableArgs(Object[] args) {
        if (valuableArgsCount == CacheConsts.INCLUDE_ALL_ARGS) {
            return new ArgsKey(args);
        }
        Object[] valuableArgs = new Object[valuableArgsCount];
        for (int i = 0; i < valuableArgsCount; i++) {
            valuableArgs[i] = args[i];
        }
        return new ArgsKey(valuableArgs);
    }


    @Override
    public boolean contains(Object[] args) {
        return argsResultMap.containsKey(getValuableArgs(args));
    }

    @Override
    public void put(Object[] args, Object value) {
        argsResultMap.put(getValuableArgs(args), value);
    }

    protected void put(ArgsKey key, Object value) {
        argsResultMap.put(key, value);
    }

    @Override
    public Object get(Object[] args) {
        return argsResultMap.get(getValuableArgs(args));
    }
}
