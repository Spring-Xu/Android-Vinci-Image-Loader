package com.vinci.imageloader.core.imagecatcher.impl;

import android.graphics.Bitmap;

import com.vinci.imageloader.core.exception.VinciException;
import com.vinci.imageloader.core.imagecatcher.Catcher;
import com.vinci.imageloader.core.imagecatcher.CatcherType;
import com.vinci.imageloader.core.memorycache.MemoryCache;

/**
 * Created by SpringXu-Git on 15/11/12.
 */
public class CacheCatcher implements Catcher<Bitmap> {

    private MemoryCache<Bitmap> memmoryCache;
    public CacheCatcher(MemoryCache<Bitmap> memmoryCache){
        this.memmoryCache = memmoryCache;
    }

    @Override
    public Bitmap get(String urlKey) throws VinciException {
        return memmoryCache.get(urlKey);
    }

    @Override
    public String getType() {
        return CatcherType.CACHE;
    }

    @Override
    public void close() throws VinciException {

    }
}
