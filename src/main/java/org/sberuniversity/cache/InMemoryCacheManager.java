package org.sberuniversity.cache;

import org.sberuniversity.annotation.CacheConsts;
import org.sberuniversity.proxy.ArgsKey;

import java.util.HashMap;

public class InMemoryCacheManager implements CacheManager {
    protected HashMap<ArgsKey, Object> argsResultMap;
    private int valuableArgsCount;

    private ArgsKey getValuableArgs(Object[] args) {
        if (valuableArgsCount == CacheConsts.INCLUDE_ALL_ARGS) {
            return new ArgsKey(args);
        }
        Object[] valuableArgs = new Object[valuableArgsCount];
        for (int i = 0; i < valuableArgsCount; i++) {
            valuableArgs[i] = args[i];
        }
        return new ArgsKey(valuableArgs);
    }

    public InMemoryCacheManager(int valuableArgsCount) {
        this.valuableArgsCount = valuableArgsCount;
        argsResultMap = new HashMap<>();
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
