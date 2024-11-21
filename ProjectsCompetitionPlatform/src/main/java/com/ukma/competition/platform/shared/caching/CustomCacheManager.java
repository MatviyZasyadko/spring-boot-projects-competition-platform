package com.ukma.competition.platform.shared.caching;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CustomCacheManager implements CacheManager {

    private final Map<String, Cache> cacheMap = new ConcurrentHashMap<>();

    @Override
    public Cache getCache(String name) {
        return cacheMap.computeIfAbsent(name, CustomCache::new);
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheMap.keySet();
    }
}
