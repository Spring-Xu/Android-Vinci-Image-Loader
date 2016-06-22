package com.vinci.imageloader.load;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.vinci.imageloader.core.bridge.BridgeFactory;
import com.vinci.imageloader.core.engine.ImageCatcher;
import com.vinci.imageloader.core.engine.ImageLoadInfo;
import com.vinci.imageloader.core.engine.ImageLoadTask;
import com.vinci.imageloader.core.engine.VinciEngine;
import com.vinci.imageloader.core.imagecatcher.CatcherType;
import com.vinci.imageloader.core.util.ImageUtils;
import com.vinci.imageloader.core.util.MLog;
import com.vinci.imageloader.core.view.HardVinciView;

/**
 * loader for user avatar
 *
 * @Modify: by SpringXu
 * @ModifyDate: 2015-1-12
 */
final class VinciProxy {
    final String TAG = VinciProxy.class.getSimpleName();
    /**
     * vinci default config
     */
    private VinciOptions vinciOptions;

    VinciEngine engine;

    public VinciProxy(VinciOptions vinciOptions) {
        this.vinciOptions = vinciOptions;
    }

    protected void init() {
        if (vinciOptions == null) {
            //set the default options
            vinciOptions = BridgeFactory.createDefaultVinciOptions();
        }

        if (engine == null) {
            engine = new VinciEngine(vinciOptions.getExecutor());
        }
    }

    public VinciOptions getVinciOptions() {
        return vinciOptions;
    }

    /**
     * @param imageView
     * @param uri
     */
    public void showImage(ImageView imageView, String uri, ImageOptions imageOptions, ImageLoadListener loadListener) {
        if (imageOptions == null) {
            init();
            imageOptions = this.vinciOptions.getDefaultImageOptions();
        }

        if (imageView == null) {
            throw new RuntimeException("imageView is null");
        }

        String mURL = ImageUtils.getCatchUrlByUri(uri);
        if (TextUtils.isEmpty(mURL)) {
            MLog.e(TAG, " mURL is null:" + uri);
            if (imageOptions.shouldShowDefaultImage()) {
                imageView.setImageResource(imageOptions.getDefaultImageResId());
            } else {
                imageView.setImageBitmap(null);
            }
            return;
        }

        if (loadListener != null) {
            loadListener.onDownloadStart(uri);
        }

        HardVinciView vinciView = new HardVinciView(imageView);

        // init cache key
        String cacheKey = ImageUtils.getCacheKey(mURL, vinciView.getWidth(), vinciView.getHeight());
        vinciView.setViewKey(cacheKey);
        engine.prepareToShowImage(vinciView, cacheKey);

        //get from memory cache
        Bitmap bitmap = (Bitmap) vinciOptions.getMemoryCache().get(cacheKey);
        if (bitmap != null && !bitmap.isRecycled()) {
            imageView.setImageBitmap(bitmap);
            if (loadListener != null) {
                loadListener.onDownloadOK(uri, bitmap);
            }
            return;
        }

        // create a image request task
        ImageLoadInfo imageLoadInfo = new ImageLoadInfo(uri, vinciOptions, imageOptions, loadListener);

        //show default image image
        setDefaultImageOrNull(vinciView, imageLoadInfo);

        // add image load task
        ImageLoadTask imageLoadTask = new ImageLoadTask(imageLoadInfo, vinciView, engine);
        engine.execute(false, false, imageLoadTask);
    }

    protected void setDefaultImageOrNull(HardVinciView vinciView, ImageLoadInfo imageLoadInfo) {
        if (imageLoadInfo.imageOptions.shouldShowDefaultImage()) {
            String url = String.valueOf(imageLoadInfo.imageOptions.getDefaultImageResId());
            String cacheKey = ImageUtils.getCacheKey(url, vinciView.getWidth(), vinciView.getHeight());
            Bitmap bitmap = (Bitmap) vinciOptions.getMemoryCache().get(cacheKey);

            if (bitmap == null || bitmap.isRecycled()) {
                ImageCatcher imageHunter = new ImageCatcher(vinciView.getWapView().getContext(), imageLoadInfo);
                bitmap = imageHunter.catchImageSync(CatcherType.RESOURCE, vinciView.getWidth(), vinciView.getHeight(),
                        String.valueOf(imageLoadInfo.imageOptions.getDefaultImageResId()), null);
            }

            vinciView.setBitmap(bitmap);
        }
    }

    public void showImage(ImageView imageView, String url, ImageOptions imageOptions) {
        if (imageOptions == null) {
            init();
            imageOptions = this.vinciOptions.getDefaultImageOptions();
        }
        showImage(imageView, url, imageOptions, null);
    }

    public void showImage(ImageView imageView, String url) {
        init();
        showImage(imageView, url, this.vinciOptions.getDefaultImageOptions(), null);
    }

    public void showImage(ImageView imageView, String url, ImageLoadListener loadListener) {
        init();
        showImage(imageView, url, this.vinciOptions.getDefaultImageOptions(), loadListener);
    }

    public void shutDown() {
        vinciOptions.getMemoryCache().clear();
        vinciOptions.getFileCache().shutDown();
        vinciOptions.getExecutor().shoutDown();
    }

    public void clearMemoryCache() {
        vinciOptions.getMemoryCache().clear();
    }
}
