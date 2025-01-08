package org.sberuniversity.proxy;

import org.sberuniversity.annotation.Cache;
import org.sberuniversity.cache.CacheManager;
import org.sberuniversity.cache.CacheManagerFabric;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CacheInvocationHandler implements InvocationHandler {
    private Object target;
    private Map<String, CacheManager> cacheManagers;
    private String rootPath;

    public CacheInvocationHandler(Object target, String rootPath) {
        this.rootPath = rootPath;
        this.target = target;
        initCacheManagers();
    }

    private void initCacheManagers() {
        CacheManagerFabric cacheManagerFabric = new CacheManagerFabric();
        cacheManagers = new HashMap<>();
        for (Class<?> inface : target.getClass().getInterfaces()) {
            for (Method method : inface.getMethods()) {
                if (method.isAnnotationPresent(Cache.class)) {
                    cacheManagers.put(method.getName(),
                            cacheManagerFabric.createManager(method, method.getAnnotation(Cache.class)));
                }
            }
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!method.isAnnotationPresent(Cache.class)) {
            System.out.println("Простой вызов");
            return method.invoke(target, args);
        } else {
            CacheManager manager = cacheManagers.get(method.getName());
            ArgsKey lockArgument = manager.getValuableArgs(args);
            synchronized (lockArgument) {
                if (manager.contains(args)) {
                    System.out.println("Взято из кэша");
                    return manager.get(args);
                } else {
                    System.out.println("Вычислено впервые");
                    Object result = method.invoke(target, args);
                    manager.put(args, result);
                    return result;
                }
            }
        }
    }


}
