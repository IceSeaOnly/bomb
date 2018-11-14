package site.binghai.framework.def;

public interface CacheService<T> {
    T load();
    void immediateExpired();
    long setExpiredSecs();
}
