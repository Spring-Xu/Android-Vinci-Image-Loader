package com.vinci.imageloader.core.consts;

/**
 * Created by SpringXu on 15/9/16.
 */
public class Const {

    public class FileCacheType {
        public static final int LRUCACHE = 0;
    }

    public class MemoryCacheType {
        public static final int LRUCACHE = 0;
    }

    public class ExecutorPoolType {
        /**
         * first-in-last-out
         */
        public static final int FILOPOOL = 0;
        /**
         * first-in-first-out
         */
        public static final int FIFOPOOL = 1;
    }

    /**
     * Image type: from resource
     */
    public static final String IMAGE_TYPE_RESOURCE = "file:///android_res/";

    /**
     * Image type: from SDCard
     */
    public static final String IMAGE_TYPE_SDCARD = "file://";

    /**
     * Image type: from provider
     */
    public static final String IMAGE_TYPE_PROVIDER = "content:";

    /**
     * Image type: from asset
     */
    public static final String IMAGE_TYPE_ASSET = "file:///android_asset/";
}
