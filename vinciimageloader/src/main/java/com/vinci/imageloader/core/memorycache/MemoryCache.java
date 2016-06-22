package com.vinci.imageloader.core.memorycache;

import com.vinci.imageloader.core.memorycache.impl.MemoryCacheOptions;

/**
 * This is used for cache
 *
 * @author Created by SpringXu on 15/9/2.
 */
public interface MemoryCache<T> {
    /**
     * Get the object by key from cache. If key is null or empty, throw {@link NullPointerException}.
     *
     * @param key
     * @return
     */
    public T get(String key);

    /**
     * Add a object to cache, and if the t is null or key is null, {@link NullPointerException} will<br/>
     * be throw
     *
     * @param t
     * @param key
     */
    public void put(String key, T t);

    /**
     * Remove a cached object from cache, and the {@link NullPointerException} will be throw if key is null.
     *
     * @param key
     * @return
     */
    public T remove(String key);

    /**
     * Try to find a cached object in cache, if found return true, else return false,and the<br/>
     * {@link NullPointerException} will be throw if key is null.
     *
     * @param key
     * @return
     */
    public boolean isExist(String key);

    /**
     * close the cache ,remove all caches
     */
    public void clear();

    /**
     * set the memory cache vinciOptions
     *
     * @param options
     */
    public void setOptions(MemoryCacheOptions options);
}
