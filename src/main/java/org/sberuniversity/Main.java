package org.sberuniversity;

import org.sberuniversity.proxy.CacheProxy;
import org.sberuniversity.service.Service;
import org.sberuniversity.service.ServiceImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon Service the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        CacheProxy cacheProxy = new CacheProxy("/");
        Service service = cacheProxy.cache(new ServiceImpl());
        ExecutorService executorService = Executors.newFixedThreadPool(15);
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                double res = service.doHardWork("work1", 1);
                System.out.println(res);
            });
            executorService.submit(() -> {
                double res = service.doHardWork("work2", 2);
                System.out.println(res);
            });
            executorService.submit(() -> {
                double res = service.doHardWork("work3", 2);
                System.out.println(res);
            });
        }
        executorService.shutdown();

    }
}