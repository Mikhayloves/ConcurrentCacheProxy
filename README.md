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

## 2. Метод ArgsKey помечен ключевым словом synchronized: 
_______________________________
сам метод:

```java
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
```
Что делает метод calcValuableArgs?
Этот метод отвечает за создание уникального ключа ArgsKey на основе переданного массива аргументов. Ключевой момент заключается в том, что метод может работать с различными потоками, каждый из которых передает свой собственный набор аргументов. Для корректной работы с этими аргументами важно, чтобы операция создания ключа была безопасной в многопоточной среде.

Почему нужен synchronized?
Ключевое слово synchronized используется для защиты критических секций кода от одновременного выполнения несколькими потоками. В данном случае, если бы метод calcValuableArgs не был синхронизирован, то могло бы возникнуть состояние гонки (race condition), когда два или более потока пытались бы одновременно создать ключи на основе одних и тех же аргументов. Это могло бы привести к непредсказуемым результатам, таким как дублирование ключей или ошибки при обращении к кешу.

Как работает synchronized?
Когда метод помечен как synchronized, JVM гарантирует, что только один поток за раз может выполнять этот метод для данного объекта. Остальные потоки, пытающиеся вызвать этот метод, будут заблокированы до тех пор, пока текущий поток не завершит выполнение метода.

Пример потенциальной проблемы без synchronized
Представим ситуацию, когда два потока одновременно вызывают метод calcValuableArgs с одними и теми же аргументами. Без синхронизации оба потока могли бы одновременно изменить содержимое переменной valuableArgs, что привело бы к созданию неверного ключа. С использованием synchronized такая ситуация исключена, поскольку только один поток может выполнять метод в конкретный момент времени.
_______________________________

Итак, использование synchronized в методе calcValuableArgs приносит следующие выгоды:

- Предотвращает состояния гонки.
- Обеспечивает корректную работу с кешем.
- Гарантирует целостность данных.
- 
Однако стоит учитывать, что это может немного замедлить работу программы из-за необходимости ожидания другими потоками. Тем не менее, эти издержки обычно оправданы ради достижения безопасности и надежности в многопоточной среде.


