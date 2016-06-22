package com.vinci.imageloader.core.imagecatcher.impl;

import android.graphics.Bitmap;

import com.vinci.imageloader.core.exception.VinciException;
import com.vinci.imageloader.core.imagecatcher.Catcher;
import com.vinci.imageloader.core.imagecatcher.CatcherType;

/**
 * Created by SpringXu-Git on 6/3/16.
 */
public class ProviderCatcher implements Catcher<Bitmap> {
    @Override
    public Bitmap get(String urlKey) throws VinciException {
        return null;
    }

    @Override
    public String getType() {
        return CatcherType.PROVIDER;
    }

    @Override
    public void close() throws VinciException {

    }
}
