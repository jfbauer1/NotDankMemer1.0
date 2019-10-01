/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package me.turulix.main.UtilClasses.SubClasses;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @param <K> key type
 * @param <V> cache item type
 * @author John Grosh (john.a.grosh@gmail.com)
 */
public class FixedCache<K, V> {
    @NotNull
    private final Map<K, V> map;
    @NotNull
    private final K[] keys;
    private int currIndex = 0;

    public FixedCache(int size) {
        this.map = new HashMap<>();
        if (size < 1) throw new IllegalArgumentException("Cache size must be at least 1!");
        //noinspection unchecked
        this.keys = (K[]) new Object[size];
    }

    @Nullable
    public V put(K key, V value) {
        if (map.containsKey(key)) {
            return map.put(key, value);
        }
        //noinspection ConstantConditions
        if (keys[currIndex] != null) {
            map.remove(keys[currIndex]);
        }
        keys[currIndex] = key;
        currIndex = (currIndex + 1) % keys.length;
        return map.put(key, value);
    }

    public V pull(K key) {
        return map.remove(key);
    }

    public V get(K key) {
        return map.get(key);
    }

    public boolean contains(K key) {
        return map.containsKey(key);
    }

    @NotNull
    public Collection<V> getValues() {
        return map.values();
    }
}