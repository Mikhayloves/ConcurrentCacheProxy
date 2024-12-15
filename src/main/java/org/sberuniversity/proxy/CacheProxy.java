package org.sberuniversity.proxy;

import java.lang.reflect.Proxy;

public class CacheProxy {
    private String cacheRootPath;

    public CacheProxy(String cacheRootPath) {
        this.cacheRootPath = cacheRootPath;
    }

    @SuppressWarnings("unchecked")
    public <T> T cache(T object) {
        return (T) Proxy.newProxyInstance(
                object.getClass().getClassLoader(),
                object.getClass().getInterfaces(),
                new CacheInvocationHandler(object, cacheRootPath)
        );
    }
}
