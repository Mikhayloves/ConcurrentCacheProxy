package org.sberuniversity.cache;

import org.sberuniversity.proxy.ArgsKey;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileCacheManager extends InMemoryCacheManager {
    private final boolean zip;
    private final String fileCachePath;

    public FileCacheManager(int valuableArgsCount, String fileCachePath, boolean zip) {
        super(valuableArgsCount);
        this.fileCachePath = fileCachePath;
        this.zip = zip;
        warmCache();
    }

    @SuppressWarnings("unchecked")
    private void warmCache() {
        File cacheFile = new File(fileCachePath);
        if (cacheFile.exists()) {
            try {
                if (zip) {
                    try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(fileCachePath))) {
                        ZipEntry entry = zipInputStream.getNextEntry();
                        String entryName = "cache.dat";
                        if (entry.getName().equals(entryName)) {
                            // Десериализация объекта из файла:
                            try (ObjectInputStream objectInputStream = new ObjectInputStream(zipInputStream)) {
                                HashMap<ArgsKey, Object> map = (HashMap<ArgsKey, Object>) objectInputStream.readObject();  // Загружаем объект
                                for (Map.Entry<ArgsKey, Object> cachePair : map.entrySet()) {
                                    super.put(cachePair.getKey(), cachePair.getValue());
                                }
                            }
                        }
                    }
                } else {
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileCachePath));
                    HashMap<ArgsKey, Object> map = (HashMap<ArgsKey, Object>) ois.readObject();
                    for (Map.Entry<ArgsKey, Object> cachePair : map.entrySet()) {
                        super.put(cachePair.getKey(), cachePair.getValue());
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void serialize() {
        try {
            if (zip) {
                File file = new File(fileCachePath);
                file.delete();
                try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(fileCachePath))) {
                    String entryName = "cache.dat";
                    ZipEntry entry = new ZipEntry(entryName);
                    zipOutputStream.putNextEntry(entry);
                    try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(zipOutputStream)) {
                        objectOutputStream.writeObject(argsResultMap);
                        System.out.println("Объект успешно сериализован в ZIP.");
                    }
                }
            } else {
                try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileCachePath))) {
                    objectOutputStream.writeObject(argsResultMap);
                    System.out.println("Произошло сохранение в файл");
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении файла: " + e.getMessage());
        }
    }


    @Override
    public boolean contains(Object[] args) {
        return super.contains(args);
    }

    @Override
    public void put(Object[] args, Object value) {
        super.put(args, value);
        serialize();
    }

    @Override
    public Object get(Object[] args) {
        return super.get(args);
    }
}
