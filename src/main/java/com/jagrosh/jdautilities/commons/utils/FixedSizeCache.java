/*
 * Developed by Turulix on 20.01.19 21:13.
 * Last modified 20.01.19 21:12.
 * Copyright (c) 2019. All rights reserved
 */

package com.jagrosh.jdautilities.commons.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple first-in-first-out key-value storage that uses a {@link java.util.HashMap HashMap} to store keys and values
 * while simultaneously registering the keys to an array to maintain a specified maximum capacity.
 *
 * <p>As new elements are inserted into the cache, older ones may be removed as a result of the cache
 * being at the maximum capacity set at instantiation.
 *
 * @author Michael Ritter
 * @since 1.3
 */
public class FixedSizeCache<K, V> {
    private final Map<K, V> map;
    private final K[] keys;
    private int currIndex = 0;

    /**
     * Constructs a new {@link com.jagrosh.jdautilities.commons.utils.FixedSizeCache FixedSizeCache} with a set maximum
     * capacity.
     *
     * <p>This entity runs on the basis of "first-in-first-out", meaning that elements inserted
     * into the newly constructed cache will remove the oldest ones if the maximum size is already being occupied.
     *
     * @param size The size of the FixedSizeCache to be created.
     */
    @SuppressWarnings("unchecked")
    public FixedSizeCache(int size) {
        this.map = new HashMap<>();
        if (size < 1) throw new IllegalArgumentException("Cache size must be at least 1!");
        this.keys = (K[]) new Object[size];
    }

    /**
     * Adds a key and pairs it with a value.
     *
     * <p>If this {@link com.jagrosh.jdautilities.commons.utils.FixedSizeCache FixedSizeCache}
     * is already at maximum occupation, this will remove the oldest element.
     *
     * <p><b>NOTE:</b> Any inner workings of {@link java.util.HashMap#put(Object, Object)
     * HashMap#put(Object, Object)} <b>still apply</b> when using this method!
     * <br>It is recommended anyone using this consult the documentation for HashMap.
     *
     * @param key   The key to pair with the value
     * @param value The value to pair with the key
     * @see java.util.HashMap#put(Object, Object) HashMap#put(Object, Object)
     */
    public void add(K key, V value) {
        if (keys[currIndex] != null) {
            map.remove(keys[currIndex]);
        }
        map.put(key, value);
        keys[currIndex] = key;
        currIndex = (currIndex + 1) % keys.length;
    }

    /**
     * Checks if this {@link com.jagrosh.jdautilities.commons.utils.FixedSizeCache FixedSizeCache} contains a key.
     *
     * <p><b>NOTE:</b> Any inner workings of {@link java.util.HashMap#containsKey(Object)
     * HashMap#containsKey(Object)} <b>still apply</b> when using this method!
     * <br>It is recommended anyone using this consult the documentation for HashMap.
     *
     * @param key The key to check for
     * @return {@code true} if the FixedSizeCache contains a key, else {@code false}
     * @see java.util.HashMap#containsKey(Object) HashMap#containsKey(Object)
     */
    public boolean contains(K key) {
        return map.containsKey(key);
    }

    /**
     * Retrieves a value from this {@link FixedSizeCache FixedSizeCache} corresponding to the specified key, or {@code
     * null} if there is no corresponding value to be retrieved.
     *
     * <p><b>NOTE:</b> Any inner workings of {@link java.util.HashMap#get(Object)
     * HashMap#get(Object)} <b>still apply</b> when using this method!
     * <br>It is recommended anyone using this consult the documentation for HashMap.
     *
     * @param key The key to retrieve a value for
     * @return A value corresponding to the provided key, or {@code null} if there was no value to get.
     * @see java.util.HashMap#get(Object) HashMap#get(Object)
     */
    public V get(K key) {
        return map.get(key);
    }
}
