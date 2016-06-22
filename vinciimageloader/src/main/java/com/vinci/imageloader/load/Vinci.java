package com.vinci.imageloader.load;

import android.widget.ImageView;

import com.vinci.imageloader.core.bridge.BridgeFactory;

/**
 * Vinci is initialized and control by the this.
 *
 * @Modify: by Spring-Xu
 * @Email: hquspring@gmail.com
 * @Date: 2015-9-4
 */
public class Vinci {

    public static final String TAG = Vinci.class.getSimpleName();

    private static Vinci mInstance;

    private VinciProxy vinciProxy;

    public static Vinci getInstance() {
        if (mInstance == null) {
            synchronized (Vinci.class) {
                if (mInstance == null) {
                    mInstance = new Vinci();
                }
            }
        }

        return mInstance;
    }

    /**
     * Constructs a new Vinci.
     */
    private Vinci() {

    }

    /**
     * Initialize Vinci by VinciOptions, this will initialize a proxy for Vinci.
     *
     * @param options
     */
    public void init(VinciOptions options) {
        if (options == null) {
            throw new NullPointerException("options is null");
        }
        vinciProxy = new VinciProxy(options);
    }

    /**
     * Setter the default vinciOptions, if this is not been called, you should use the method witch has the {@link VinciOptions}.
     * Such as {@link #showImage(ImageView, String, ImageOptions)}.
     *
     * @return
     */
    public void initByDefaultOptions() {
        VinciOptions options = BridgeFactory.createDefaultVinciOptions();
        vinciProxy = new VinciProxy(options);
    }

    /**
     * Getter the current VinciOptions.
     * If this is not initialize at first, throw RuntimeException.
     * @return
     */
    public VinciOptions getVinciOptions() {
        checkInitOptions();
        return vinciProxy.getVinciOptions();
    }

    /**
     * Show image from net work or SDCard, resource,provider. if the url start with {@link com.vinci.imageloader.core.consts.Const#IMAGE_TYPE_RESOURCE}, it is resource image;
     * If the url start with {@link com.vinci.imageloader.core.consts.Const#IMAGE_TYPE_SDCARD},it is a image at SDCard, or if the url start with {@link com.vinci.imageloader.core.imagecatcher.CatcherType},
     * it is a Image from provider. Default, the url is a web url.
     * If this is not initialize at first, throw RuntimeException.
     *
     * @param imageView
     * @param url
     * @param imageOptions
     * @param loadListener
     */
    public void showImage(ImageView imageView, String url, ImageOptions imageOptions, ImageLoadListener loadListener) {
        checkInitOptions();
        vinciProxy.showImage(imageView, url, imageOptions, loadListener);
    }

    /**
     * Show image from net work or SDCard, resource,provider. if the url start with {@link com.vinci.imageloader.core.consts.Const#IMAGE_TYPE_RESOURCE}, it is resource image;
     * If the url start with {@link com.vinci.imageloader.core.consts.Const#IMAGE_TYPE_SDCARD},it is a image at SDCard, or if the url start with {@link com.vinci.imageloader.core.consts.Const#IMAGE_TYPE_PROVIDER},
     * it is a Image from provider. Default, the url is a web url.
     * If this is not initialize at first, throw RuntimeException.
     *
     * @param imageView
     * @param url
     * @param imageOptions
     */
    public void showImage(ImageView imageView, String url, ImageOptions imageOptions) {
        checkInitOptions();
        vinciProxy.showImage(imageView, url, imageOptions);
    }

    /**
     * Show image from net work or SDCard, resource,provider. if the url start with {@link com.vinci.imageloader.core.consts.Const#IMAGE_TYPE_RESOURCE}, it is resource image;
     * If the url start with {@link com.vinci.imageloader.core.consts.Const#IMAGE_TYPE_SDCARD},it is a image at SDCard, or if the url start with {@link com.vinci.imageloader.core.consts.Const#IMAGE_TYPE_PROVIDER},
     * it is a Image from provider. Default, the url is a web url.
     * If this is not initialize at first, throw RuntimeException.
     *
     * @param imageView
     * @param url
     */
    public void showImage(ImageView imageView, String url) {
        checkInitOptions();
        vinciProxy.showImage(imageView, url);
    }

    /**
     * Show image from net work or SDCard, resource,provider. if the url start with {@link com.vinci.imageloader.core.consts.Const#IMAGE_TYPE_RESOURCE}, it is resource image;
     * If the url start with {@link com.vinci.imageloader.core.consts.Const#IMAGE_TYPE_SDCARD},it is a image at SDCard, or if the url start with {@link com.vinci.imageloader.core.consts.Const#IMAGE_TYPE_PROVIDER},
     * it is a Image from provider. Default, the url is a web url.
     * If this is not initialize at first, throw RuntimeException.
     *
     * @param imageView
     * @param url
     * @param loadListener
     */
    public void showImage(ImageView imageView, String url, ImageLoadListener loadListener) {
        checkInitOptions();
        vinciProxy.showImage(imageView, url, loadListener);
    }

    /**
     * Shut down Vinci, close all the caches and threads.
     * If this is not initialize at first, throw RuntimeException.
     */
    public void shutDown() {
        checkInitOptions();
        vinciProxy.shutDown();
        mInstance = null;
    }

    /**
     * Clear Vinci's memory cache.
     * If this is not initialize at first, throw RuntimeException.
     */
    public void clearMemoryCache() {
        checkInitOptions();
        vinciProxy.clearMemoryCache();
    }

    private void checkInitOptions() {
        if (vinciProxy == null) {
            throw new RuntimeException("Vinci is not initialized");
        }
    }
}
