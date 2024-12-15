package org.sberuniversity.service;

import org.sberuniversity.annotation.Cache;
import org.sberuniversity.pathcache.CacheType;

public interface Service {
    @Cache(cache = CacheType.FILE, fileCacheName = "cache.zip", zip = true)
    Double doHardWork(String work, Object param);
}
