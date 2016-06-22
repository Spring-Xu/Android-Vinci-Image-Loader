package com.vinci.imageloader.load;

import com.vinci.imageloader.core.filecache.FileCache;
import com.vinci.imageloader.core.memorycache.MemoryCache;
import com.vinci.imageloader.core.thread.CustomExecutor;

/**
 * Created by SpringXu on 15/9/3.
 */
public class VinciOptions {
    /**
     * This is file cache, it used for image file cache
     */
    private FileCache fileCache;

    /**
     * This member cache will cache the bitmaps
     */
    private MemoryCache memoryCache;

    /**
     * the thread executor
     */
    private CustomExecutor executor;


    /**
     * Show image default vinciOptions.
     */
    private ImageOptions defaultImageOptions;

    private VinciOptions(Builder builder) {
        this.fileCache = builder.mFileCache;
        this.memoryCache = builder.mMemoryCache;
        this.executor = builder.executor;
        this.defaultImageOptions = builder.defaultImageOptions;
    }

    private VinciOptions() {
    }

    public ImageOptions getDefaultImageOptions() {
        return defaultImageOptions;
    }

    public FileCache getFileCache() {
        return fileCache;
    }

    public MemoryCache getMemoryCache() {
        return memoryCache;
    }

    public CustomExecutor getExecutor() {
        return executor;
    }

    public void setFileCache(FileCache fileCache) {
        this.fileCache = fileCache;
    }

    public void setMemoryCache(MemoryCache memoryCache) {
        this.memoryCache = memoryCache;
    }

    public void setExecutor(CustomExecutor executor) {
        this.executor = executor;
    }

    public void setDefaultImageOptions(ImageOptions defaultImageOptions) {
        this.defaultImageOptions = defaultImageOptions;
    }

    public static class Builder {
        /**
         * This is file cache, it used for image file cache
         */
        private FileCache mFileCache;

        /**
         * This member cache will cache the bitmaps
         */
        private MemoryCache mMemoryCache;

        /**
         * the thread executor
         */
        private CustomExecutor executor;

        /**
         * default image vinciOptions.
         */
        private ImageOptions defaultImageOptions;

        public Builder() {
        }

        public Builder setFileCache(FileCache mFileCache) {
            this.mFileCache = mFileCache;
            return this;
        }

        public Builder setMemmoryCache(MemoryCache mMemoryCache) {
            this.mMemoryCache = mMemoryCache;
            return this;
        }

        public Builder setExecutor(CustomExecutor executor) {
            this.executor = executor;
            return this;
        }

        public Builder setDefaultImageOptions(ImageOptions defaultImageOptions) {
            this.defaultImageOptions = defaultImageOptions;
            return this;
        }

        public VinciOptions build() {
            return new VinciOptions(this);
        }
    }
}
