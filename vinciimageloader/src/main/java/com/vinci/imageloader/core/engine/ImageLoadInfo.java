package com.vinci.imageloader.core.engine;

import com.vinci.imageloader.load.ImageLoadListener;
import com.vinci.imageloader.load.ImageOptions;
import com.vinci.imageloader.load.VinciOptions;

/**
 * Created by SpringXu on 6/16/16.
 */
public class ImageLoadInfo {
    public String mUri;
    public VinciOptions vinciOptions;
    public ImageOptions imageOptions;
    public ImageLoadListener listener;

    public ImageLoadInfo(String uri, VinciOptions options, ImageOptions imageOptions, ImageLoadListener listener) {
        this.mUri = uri;
        this.vinciOptions = options;
        this.listener = listener;
        this.imageOptions = imageOptions;
    }
}
