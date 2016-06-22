package com.vinci.imageloader.core.memorycache.impl;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

import com.vinci.imageloader.core.consts.DefaultConfig;
import com.vinci.imageloader.core.memorycache.MemoryCache;
import com.vinci.imageloader.core.util.MLog;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by SpringXu on 15/9/2.
 */
public class LRUBitmapMemoryCache implements MemoryCache<Bitmap> {

    private static final String TAG = "LRUBitmapMemoryCache";

    private MemoryCacheOptions options;

    private LruCache<String, Bitmap> mMemoryCache;

    private LinkedHashMap<String, SoftReference<Bitmap>> mSoftCache;

    ReadWriteLock lock = new ReentrantReadWriteLock();
    Lock readLock = lock.readLock();
    Lock writeLock = lock.writeLock();

    /**
     * Create a instance by default config
     */
    public LRUBitmapMemoryCache() {
        options = new MemoryCacheOptions.Builder()
                .setMaxCacheCount(DefaultConfig.MemoryCacheDefaultOptions.MAX_CACHE_COUNT)
                .setMaxCacheSize(DefaultConfig.MemoryCacheDefaultOptions.MAX_CACHE_SIZE)
                .setUseCache(DefaultConfig.MemoryCacheDefaultOptions.USE_CACHE)
                .build();
        initializeCache();
    }

    /**
     * set a vinciOptions as you set
     *
     * @param defaultOptions
     */
    @Override
    public void setOptions(MemoryCacheOptions defaultOptions) {
        this.options = defaultOptions;
        initializeCache();
    }

    private void initializeCache() {

        if (options == null) {
            throw new RuntimeException("LRUBitmapMemoryCache#vinciOptions is null");
        }

        if (options.getMaxCacheSize() == 0) {
            throw new RuntimeException("LRUBitmapMemoryCache#max cache size is o");
        }

        if (options.getMaxCacheCount() == 0) {
            throw new RuntimeException("LRUBitmapMemoryCache#max cache count is o");
        }

        MLog.d(TAG, "maxSize:" + options.getMaxCacheSize() + " maxCount:" + options.getMaxCacheCount());
        mMemoryCache = new LruCache<String, Bitmap>(options.getMaxCacheSize()) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getRowBytes() * bitmap.getHeight();
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
                if (oldValue != null) {
                    // the allocation is full, move bitmap to soft reference
                    mSoftCache.put(key, new SoftReference<Bitmap>(oldValue));
                }
            }
        };

        mSoftCache = new LinkedHashMap<String, SoftReference<Bitmap>>(
                options.getMaxCacheCount(), 0.75f, true) {
            private static final long serialVersionUID = 6040103833179403725L;

            @Override
            protected boolean removeEldestEntry(
                    Entry<String, SoftReference<Bitmap>> eldest) {
                if (size() > options.getMaxCacheCount()) {
                    return true;
                }

                return false;
            }
        };
    }

    @Override
    public Bitmap get(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }

        readLock.lock();
        Bitmap bitmap = null;

        try {
            // get bitmap from mMemoryCache
            bitmap = mMemoryCache.get(key);

            if (bitmap != null && !bitmap.isRecycled()) {
                // LRU : refresh this bitmap position
                mMemoryCache.remove(key);
                mMemoryCache.put(key, bitmap);

            } else {
                // get bitmap from mSoftCache
                SoftReference<Bitmap> bitmapReference = mSoftCache.get(key);
                if (bitmapReference != null) {
                    bitmap = bitmapReference.get();

                    if (bitmap != null && !bitmap.isRecycled()) {
                        // move bitmap to mSoftCache
                        mMemoryCache.put(key, bitmap);
                        mSoftCache.remove(key);

                    } else {
                        // is recycled
                        mSoftCache.remove(key);
                    }
                }
            }

        } catch (Exception e) {
            MLog.e(e.getMessage());

        } finally {
            readLock.unlock();
        }

        return bitmap;
    }

    @Override
    public boolean isExist(String key) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }

        readLock.lock();

        boolean regTag = false;
        try {
            Bitmap bitmap;

            // get bitmap from mMemoryCache
            bitmap = mMemoryCache.get(key);
            if (bitmap != null && !bitmap.isRecycled()) {
                regTag = true;

            } else {
                // get bitmap from mSoftCache
                SoftReference<Bitmap> bitmapReference = mSoftCache.get(key);
                if (bitmapReference != null) {
                    bitmap = bitmapReference.get();
                    if (bitmap != null && !bitmap.isRecycled()) {
                        regTag = true;
                    }
                }
            }
        } catch (Exception e) {
            MLog.e(e.getMessage());
        } finally {
            readLock.unlock();
        }

        return regTag;
    }

    @Override
    public Bitmap remove(String key) {
        writeLock.lock();
        Bitmap bitmap = null;
        try {
            // remove bitmap from mMemoryCache
            bitmap = mMemoryCache.remove(key);
            // remove bitmap from mSoftCache
            if (bitmap != null && !bitmap.isRecycled()) {
                mSoftCache.remove(key);

            } else {
                SoftReference<Bitmap> bitmapReference = mSoftCache.remove(key);
                if (bitmapReference != null) {
                    bitmap = bitmapReference.get();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            writeLock.unlock();
        }

        return bitmap;
    }

    @Override
    public void put(String key, Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }

        writeLock.lock();
        try {
            // thread synchronize
            mMemoryCache.put(key, bitmap);

        } catch (Exception e) {
            MLog.e(e.getMessage());

        } finally {
            writeLock.unlock();
        }

    }

    @Override
    public void clear() {
        writeLock.lock();

        try {
            mMemoryCache.evictAll();
            mSoftCache.clear();
        } catch (Exception e) {
            MLog.e(e.getMessage());

        } finally {
            writeLock.unlock();
        }

    }
}
