package org.sberuniversity.cache;

import org.sberuniversity.annotation.Cache;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class CacheManagerFabric {

    public CacheManager createManager(Method method, Cache cacheInfo) {
        switch (cacheInfo.cache()) {
            case IN_MEMORY -> {
                if (cacheInfo.zip()) {
                    throw new RuntimeException("Не удалось заархивировать файл в памяти");
                }
                return new InMemoryCacheManager(1);
            }
            case FILE -> {
                validateMethodOnSerialization(method);
                if (cacheInfo.fileCacheName().isBlank()) {
                    throw new RuntimeException("Имя файла не может быть пустым");
                }
                String path;
                if (cacheInfo.fileCachePath().isBlank()) {
                    path = "";
                } else {
                    path = cacheInfo.fileCachePath() + "\\";
                }
                return new FileCacheManager(cacheInfo.valuableArgCount(),
                        path + cacheInfo.fileCacheName(),
                        cacheInfo.zip());
            }
            default -> {
                throw new RuntimeException("Иной аргумент");
            }
        }
    }

    //TODO Проверка метода на сериализуемость
    private void validateMethodOnSerialization(Method method) {
        validateTypeOnSerializable(method.getReturnType());
        for (Parameter parameter : method.getParameters()) {
            if (parameter.getType().equals(Object.class))
                continue;
            validateTypeOnSerializable(parameter.getType());
        }
    }

    //TODO Проверка типа на сериализуемость
    private boolean validateTypeOnSerializable(Class<?> type) {
        boolean isSerializable = false;
        for (Class<?> inface : type.getInterfaces()) {
            if (inface.equals(Serializable.class)) {
                isSerializable = true;
                break;
            }
        }
        if (!isSerializable && type.getSuperclass() != null && !type.getSuperclass().equals(Object.class)) {
            isSerializable = validateTypeOnSerializable(type.getSuperclass());
        }
        if (!isSerializable) {
            throw new RuntimeException(type.getTypeName() + " is not serializable");
        }
        return true;
    }
}
