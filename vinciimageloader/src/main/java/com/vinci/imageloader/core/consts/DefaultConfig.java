package com.vinci.imageloader.core.consts;

import android.os.Environment;


/**
 * Created by SpringXu on 15/9/16.
 */
public class DefaultConfig {

    /**
     * Created by SpringXu on 15/9/2.
     */
    public static class MemoryCacheDefaultOptions {
        public static final int MAX_CACHE_SIZE = (int) Runtime.getRuntime().maxMemory() / 1024 / 6;

        /**
         * The max cache weak objects count
         */
        public static final int MAX_CACHE_COUNT = 10;

        /**
         * Whether you can you memory cache, you can control by this switch, default it is true
         */
        public static final boolean USE_CACHE = true;

    }

    public static class ExecutorPoolDefaultOptions {
        /**
         * max execute threads count at the same time
         */
        public static final int DEFAULT_MAX_EXECUTING_SIZE = Runtime.getRuntime().availableProcessors() - 2;
        /**
         * max pool size
         */
        public static final int DEFAULT_MAX_POOL_SIZE = 128;
        /**
         * Max run time in mills(2 second).
         */
        public static final int DEFAULT_MAX_RUN_TIME = 2000;
    }


    public static class FileCacheDefaultOptions {
        /**
         * the file cache root path
         */
        public static final String ROOT_PATH = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED) ?
                Environment.getExternalStorageDirectory().getPath() + "/Vinci" :
                Environment.getDownloadCacheDirectory().getPath() + "/Vinci";
        /**
         * file cache count
         */
        public static final int MAX_FILE_COUNT = 1000;
        /**
         * file cache max size: byte
         */
        public static final int MAX_CACHE_SIZE = 50 * 1024 * 1024;//50MB
        /**
         * if it is false, will not cache files
         */
        public static final boolean USER_FILE_CACHE = true;
    }

    /**
     * Image options default config,
     */
    public static class DefaultImageOptions {
        /**
         * Setter the memory cache use state
         */
        public static final boolean USE_MEMORY_CACHE = true;
        /**
         * Setter the file cache use state
         */
        public static final boolean USE_FILE_CACHE = true;
        /**
         * default temp image resource id
         */
        public static final int DEFAULT_IMAGE_RES_ID = 0;
        /**
         * load failed state: show image resource id
         */
        public static final int ERROR_IMAGE_RES_ID = 0;
        /**
         * loading state: show image resource id
         */
        public static final int LOADING_IMAGE_RES_ID = 0;

        /**
         * This tag is used to control whether it will show original image
         */
        public static final boolean SHOW_ORIGINAL = false;

        /**
         * Round image corner size
         */
        public static final int CORNER_SIZE = 0;
    }
}
