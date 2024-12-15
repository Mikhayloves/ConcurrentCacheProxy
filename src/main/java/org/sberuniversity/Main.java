package org.sberuniversity;

import org.sberuniversity.proxy.CacheProxy;
import org.sberuniversity.service.Service;
import org.sberuniversity.service.ServiceImpl;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon Service the gutter.
public class Main {
    public static void main(String[] args) {
        CacheProxy cacheProxy = new CacheProxy("/");
        Service service = cacheProxy.cache(new ServiceImpl());
        double res1 = service.doHardWork("work1",1);
        double res2 = service.doHardWork("work1",1);
        double res3 = service.doHardWork("work2",3);
        double res4 = service.doHardWork("work3",3);
        double res5 = service.doHardWork("work3",4);





        System.out.println(res1);
        System.out.println(res2);
        System.out.println(res3);
        System.out.println(res5);
        System.out.println(res4);



    }
}