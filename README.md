# Домашнее задание №13
_______________________________
## Тема задания:
Модифицировать кэш, из предыдущих домашних заданий по рефлексии и сериализации (описание ниже), сделать его конкурентным. 
Должна быть возможность не лочить весь кэш, а только тот элемент который в данный момент добавляется или удаляется.
_______________________________
ссылка на кэширующий прокси ---> https://github.com/Mikhayloves/serialization
_______________________________
## Что было реализованно:
## 1. В метод invoke был добавлен блок синхронизации:
_______________________________
сам метод:
```java
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
```
_______________________________

блок:
```java 
synchronized (lockArgument) {
}
```
Ключевое слово synchronized используется для блокировки объекта lockArgument, чтобы гарантировать, что только один поток за раз может выполнять логику проверки и обновления кэша для конкретного набора аргументов. Это позволяет избежать состояния гонки, когда два потока могли бы одновременно попытаться вычислить одно и то же значение и сохранить его в кэш.

Таким образом, каждый поток, который вызывает метод с одинаковыми аргументами, будет ожидать, пока предыдущий поток не завершит свою работу с данным набором аргументов.

## 2.
