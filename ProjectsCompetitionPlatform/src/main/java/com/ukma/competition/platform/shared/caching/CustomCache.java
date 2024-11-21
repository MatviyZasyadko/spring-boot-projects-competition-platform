package com.ukma.competition.platform.shared.caching;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.Cache;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomCache implements Cache {

    private final String name;
    private final ConcurrentMap<Object, CacheValueWrapper> store = new ConcurrentHashMap<>();

    public CustomCache(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return store;
    }

    @Override
    public ValueWrapper get(Object key) {
        CacheValueWrapper wrapper = store.get(key);
        if (wrapper == null || wrapper.isExpired()) {
            store.remove(key);
            return null;
        }
        return wrapper;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        ValueWrapper valueWrapper = get(key);
        return (valueWrapper != null && type.isInstance(valueWrapper.get())) ? type.cast(valueWrapper.get()) : null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        CacheValueWrapper wrapper = store.get(key);
        if (wrapper != null && !wrapper.isExpired()) {
            return (T) wrapper.get();
        }
        try {
            T value = valueLoader.call();
            put(key, value);
            return value;
        } catch (Exception e) {
            throw new Cache.ValueRetrievalException(key, valueLoader, e);
        }
    }

    @Override
    public void put(Object key, Object value) {
        store.put(key, new CacheValueWrapper(value));
    }

    @Override
    public void evict(Object key) {
        store.remove(key);
    }

    @Override
    public void clear() {
        store.clear();
    }

    static class CacheValueWrapper implements ValueWrapper {

        private final Object value;
        private final long expirationTime;

        public CacheValueWrapper(Object value) {
            this(value, 120000);
        }

        public CacheValueWrapper(Object value, long ttlMillis) {
            this.value = value;
            this.expirationTime = System.currentTimeMillis() + ttlMillis;
        }

        @Override
        public Object get() {
            return value;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expirationTime;
        }
    }
}
