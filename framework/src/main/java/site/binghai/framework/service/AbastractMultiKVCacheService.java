package site.binghai.framework.service;

import javafx.util.Pair;
import site.binghai.framework.utils.BaseBean;
import site.binghai.framework.utils.TimeTools;

import java.util.concurrent.ConcurrentHashMap;

public abstract class AbastractMultiKVCacheService<K, V> extends BaseBean {
    private long expiredSecs = 0L;
    private ConcurrentHashMap<K, Pair<Long, V>> cache;

    public V get(K key) {
        return get(key, false);
    }

    public V get(K key, boolean nocache) {
        if (cache == null) {
            init();
        }
        Pair<Long, V> val = cache.get(key);
        if (nocache || val == null || expired(val)) {
            cache.put(key, loadData(key));
        }

        return cache.get(key).getValue();
    }

    private boolean expired(Pair<Long, V> val) {
        if (expiredSecs < 0) { return false; }
        return now() - val.getKey() > expiredSecs;
    }

    private synchronized void init() {
        if (cache != null) { return; }
        cache = new ConcurrentHashMap<>();
        expiredSecs = setExpiredSecs() * 1000;
    }

    protected abstract long setExpiredSecs();

    private synchronized Pair<Long, V> loadData(K key) {
        long lastCallTime = now();
        logger.info("{} reload data at {}", key, TimeTools.now());
        V v = load(key);
        if (v == null) {
            throw new RuntimeException("no data loaded!");
        }
        return new Pair(lastCallTime, v);
    }

    protected abstract V load(K key);
}