package com.vinci.imageloader.core.filecache.impl;

import com.vinci.imageloader.core.consts.DefaultConfig;

/**
 * Created by Spring-Xu on 15/8/24.
 */
public final class FileCacheOptions {
    /**
     * the file cache root path
     */
    private String cacheRootPath;
    /**
     * file cache count
     */
    private int maxFileCount;
    /**
     * file cache max size: byte
     */
    private int maxCacheSize;
    /**
     * if it is false, will not cache files
     */
    private boolean isUseFileCache;

    public String getCacheRootPath() {
        return cacheRootPath;
    }

    public void setCacheRootPath(String cacheRootPath) {
        this.cacheRootPath = cacheRootPath;
    }

    public int getMaxFileCount() {
        return maxFileCount;
    }

    public void setMaxFileCount(int maxFileCount) {
        this.maxFileCount = maxFileCount;
    }

    /**
     * cache size in bytes
     *
     * @return
     */
    public int getMaxCacheSize() {
        return maxCacheSize;
    }

    public void setMaxCacheSize(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }

    public boolean isUseFileCache() {
        return isUseFileCache;
    }

    public void setIsUseFileCache(boolean isUseFileCache) {
        this.isUseFileCache = isUseFileCache;
    }

    private FileCacheOptions(Builder builder) {
        setCacheRootPath(builder.getCacheRootPath());
        setIsUseFileCache(builder.isUseFileCache());
        setMaxCacheSize(builder.getMaxCacheSize());
        setMaxFileCount(builder.getMaxFileCount());
    }

    /**
     * This is the vinciOptions set builder, we can create the vinciOptions by this method
     */
    public static class Builder {
        private String cacheRootPath = DefaultConfig.FileCacheDefaultOptions.ROOT_PATH;
        private int maxFileCount = DefaultConfig.FileCacheDefaultOptions.MAX_FILE_COUNT;
        private int maxCacheSize = DefaultConfig.FileCacheDefaultOptions.MAX_CACHE_SIZE;
        private boolean isUseFileCache = DefaultConfig.FileCacheDefaultOptions.USER_FILE_CACHE;

        public Builder() {
        }

        public String getCacheRootPath() {
            return cacheRootPath;
        }

        public Builder setCacheRootPath(String cacheRootPath) {
            this.cacheRootPath = cacheRootPath;
            return this;
        }

        public int getMaxFileCount() {
            return maxFileCount;
        }

        public Builder setMaxFileCount(int maxFileCount) {
            this.maxFileCount = maxFileCount;
            return this;
        }

        public int getMaxCacheSize() {
            return maxCacheSize;
        }

        public Builder setMaxCacheSize(int maxCacheSize) {
            this.maxCacheSize = maxCacheSize;
            return this;
        }

        public boolean isUseFileCache() {
            return isUseFileCache;
        }

        public Builder setIsUseFileCache(boolean isUseFileCache) {
            this.isUseFileCache = isUseFileCache;
            return this;
        }

        public FileCacheOptions builder() {
            return new FileCacheOptions(this);
        }
    }
}
