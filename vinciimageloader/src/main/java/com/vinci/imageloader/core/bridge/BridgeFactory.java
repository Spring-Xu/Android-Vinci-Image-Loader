package com.vinci.imageloader.core.bridge;

import android.graphics.Bitmap;

import com.vinci.imageloader.core.consts.Const;
import com.vinci.imageloader.core.consts.DefaultConfig;
import com.vinci.imageloader.core.filecache.FileCache;
import com.vinci.imageloader.core.filecache.impl.LRUFileCache;
import com.vinci.imageloader.core.memorycache.MemoryCache;
import com.vinci.imageloader.core.memorycache.impl.LRUBitmapMemoryCache;
import com.vinci.imageloader.core.thread.CustomExecutor;
import com.vinci.imageloader.core.thread.impl.FIFOThreadPoolExecutor;
import com.vinci.imageloader.core.thread.impl.FILOThreadPoolExecutor;
import com.vinci.imageloader.load.ImageOptions;
import com.vinci.imageloader.load.VinciOptions;

/**
 * Created by SpringXu on 15/9/16.
 */
public class BridgeFactory {


    /**
     * Create file cache by cache type, you can see {@link Const.FileCacheType}, it will be configed by default
     *
     * @param cacheType
     * @return
     */
    public static FileCache createFileCache(int cacheType) {
        FileCache cache = null;
        switch (cacheType) {
            case Const.FileCacheType.LRUCACHE:
                cache = new LRUFileCache();
                break;

            default:
                cache = new LRUFileCache();
                break;
        }

        return cache;
    }

    /**
     * Create memory cache by type, the memory cache is configed with default
     *
     * @param cacheType
     * @return
     */
    public static MemoryCache<Bitmap> createMemoryCache(int cacheType) {
        MemoryCache<Bitmap> cache = null;
        switch (cacheType) {
            case Const.MemoryCacheType.LRUCACHE:
                cache = new LRUBitmapMemoryCache();
                break;

            default:
                cache = new LRUBitmapMemoryCache();
                break;
        }

        return cache;
    }

    /**
     * create executor by type , the executor is configed by default config
     *
     * @param executorType
     * @return
     */
    public static CustomExecutor createExecutor(int executorType) {
        CustomExecutor executor = null;
        switch (executorType) {
            case Const.ExecutorPoolType.FILOPOOL:
                executor = new FILOThreadPoolExecutor();
                break;

            case Const.ExecutorPoolType.FIFOPOOL:
                executor = new FIFOThreadPoolExecutor();
                break;

            default:
                executor = new FILOThreadPoolExecutor();
                break;
        }

        return executor;
    }

    public static VinciOptions createDefaultVinciOptions() {
        FileCache fileCache = createFileCache(Const.FileCacheType.LRUCACHE);
        MemoryCache memoryCache = createMemoryCache(Const.MemoryCacheType.LRUCACHE);
        CustomExecutor executor = createExecutor(Const.ExecutorPoolType.FILOPOOL);
        ImageOptions defaultImageOptions = new ImageOptions.Builder()
                .setUseFileCache(DefaultConfig.DefaultImageOptions.USE_FILE_CACHE)
                .setUseMemoryCache(DefaultConfig.DefaultImageOptions.USE_MEMORY_CACHE)
                .setShowOriginal(DefaultConfig.DefaultImageOptions.SHOW_ORIGINAL)
                .setDefaultImageResId(DefaultConfig.DefaultImageOptions.DEFAULT_IMAGE_RES_ID)
                .setLoadingImageResId(DefaultConfig.DefaultImageOptions.LOADING_IMAGE_RES_ID)
                .setErrorImageResId(DefaultConfig.DefaultImageOptions.ERROR_IMAGE_RES_ID)
                .setCornerSize(DefaultConfig.DefaultImageOptions.CORNER_SIZE)
                .build();

        VinciOptions vinciOptions = new VinciOptions.Builder()
                .setDefaultImageOptions(defaultImageOptions)
                .setExecutor(executor)
                .setFileCache(fileCache)
                .setMemmoryCache(memoryCache)
                .build();

        return vinciOptions;
    }
}
