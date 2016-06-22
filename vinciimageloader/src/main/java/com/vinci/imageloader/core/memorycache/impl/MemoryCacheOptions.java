package com.vinci.imageloader.core.memorycache.impl;

import com.vinci.imageloader.core.consts.DefaultConfig;

/**
 * The {@link LRUBitmapMemoryCache} will be config by this vinciOptions.
 * Created by SpringXu on 15/9/2.
 */
public class MemoryCacheOptions {

    /**
     * the max cache memory size(Byte)
     */
    private int maxCacheSize;

    /**
     * The max cache weak objects count
     */
    private int maxCacheCount;

    /**
     * Whether you can you memory cache, you can control by this switch, default it is true
     */
    private boolean useCache;

    private MemoryCacheOptions(Builder builder) {
        this.maxCacheSize = builder.maxCacheSize;
        this.maxCacheCount = builder.maxCacheCount;
        this.useCache = builder.useCache;
    }

    public int getMaxCacheCount() {
        return maxCacheCount;
    }

    public boolean isUseCache() {
        return useCache;
    }

    public int getMaxCacheSize() {
        return maxCacheSize;
    }

    public void setMaxCacheSize(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }

    public void setMaxCacheCount(int maxCacheCount) {
        this.maxCacheCount = maxCacheCount;
    }

    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
    }

    public static class Builder {
        /**
         * the max cache memory size(Byte)
         */
        private int maxCacheSize = DefaultConfig.MemoryCacheDefaultOptions.MAX_CACHE_SIZE;

        /**
         * The max weak cache objects count
         */
        private int maxCacheCount = DefaultConfig.MemoryCacheDefaultOptions.MAX_CACHE_COUNT;

        /**
         * Whether you can you memory cache, you can control by this switch, default it is true
         */
        private boolean useCache = DefaultConfig.MemoryCacheDefaultOptions.USE_CACHE;

        /**
         * Set the max cache weak objects' count
         *
         * @param maxCacheCount
         * @return
         */
        public Builder setMaxCacheCount(int maxCacheCount) {
            this.maxCacheCount = maxCacheCount;
            return this;
        }

        /**
         * Set the max cache size in memory(Byte)
         *
         * @param maxCacheSize
         * @return
         */
        public Builder setMaxCacheSize(int maxCacheSize) {
            this.maxCacheSize = maxCacheSize;
            return this;
        }

        /**
         * The cache Switch
         *
         * @param useCache
         * @return
         */
        public Builder setUseCache(boolean useCache) {
            this.useCache = useCache;
            return this;
        }

        public Builder() {
        }

        /**
         * Get MemoryCacheOptions by this Builder's config
         *
         * @return
         */
        public MemoryCacheOptions build() {
            return new MemoryCacheOptions(this);
        }
    }
}
