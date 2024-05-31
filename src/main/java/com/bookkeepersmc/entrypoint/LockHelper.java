package com.bookkeepersmc.entrypoint;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;

public class LockHelper<K,V> {
    public static <K, V> LockHelper<K, V> withHashMap() {
        // convert size to capacity according to default load factor
        return new LockHelper<>(size -> new HashMap<>((size + 2) * 4 / 3));
    }

    public static <K, V> LockHelper<K, V> withIdentityHashMap() {
        return new LockHelper<>(IdentityHashMap::new);
    }

    private final IntFunction<Map<K, V>> mapConstructor;

    private final Map<K, V> backingMap;
    @Nullable
    private volatile Map<K, V> readOnlyView = null;
    private Object lock = new Object();

    private LockHelper(IntFunction<Map<K, V>> mapConstructor) {
        this.mapConstructor = mapConstructor;
        this.backingMap = mapConstructor.apply(32);
    }

    public Map<K, V> getReadMap() {
        var map = readOnlyView;
        if (map == null) {
            // Need to update the read map
            synchronized (lock) {
                var updatedMap = mapConstructor.apply(backingMap.size());
                updatedMap.putAll(backingMap);
                readOnlyView = map = updatedMap;
            }
        }
        return map;
    }

    public V get(K key) {
        return getReadMap().get(key);
    }

    public boolean containsKey(K key) {
        return getReadMap().containsKey(key);
    }

    public V computeIfAbsent(K key, Function<K, V> factory) {
        return computeIfAbsent(key, factory, Function.identity());
    }

    public <I> V computeIfAbsent(K key, Function<K, I> factory, Function<I, V> finalizer) {
        // Try lock-free get first
        var ret = get(key);
        if (ret != null)
            return ret;

        var intermediate = factory.apply(key);

        synchronized (lock) {
            ret = backingMap.get(key);
            if (ret == null) {
                ret = finalizer.apply(intermediate);
                backingMap.put(key, ret);
                readOnlyView = null;
            }
        }

        return ret;
    }

    public void clearAll() {
        backingMap.clear();
        readOnlyView = null;
        lock = new Object();
    }
}
