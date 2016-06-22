package com.vinci.imageloader.core.engine;

import android.content.Context;
import android.graphics.Bitmap;

import com.vinci.imageloader.core.exception.VinciException;
import com.vinci.imageloader.core.filecache.DownLoadProgressListener;
import com.vinci.imageloader.core.filecache.FileCache;
import com.vinci.imageloader.core.imagecatcher.Catcher;
import com.vinci.imageloader.core.imagecatcher.CatcherType;
import com.vinci.imageloader.core.imagecatcher.impl.AssetCatcher;
import com.vinci.imageloader.core.imagecatcher.impl.CacheCatcher;
import com.vinci.imageloader.core.imagecatcher.impl.NetCatcher;
import com.vinci.imageloader.core.imagecatcher.impl.ProviderCatcher;
import com.vinci.imageloader.core.imagecatcher.impl.ResourceCatcher;
import com.vinci.imageloader.core.imagecatcher.impl.SDCardCatcher;
import com.vinci.imageloader.core.memorycache.MemoryCache;
import com.vinci.imageloader.core.util.ImageUtils;
import com.vinci.imageloader.core.util.MLog;
import com.vinci.imageloader.load.ImageOptions;

/**
 * Created by SpringXu-Git on 6/3/16.
 */
public class ImageCatcher {

    private FileCache mFileCache;
    private MemoryCache<Bitmap> mMemoryCache;
    private Context context;
    private ImageOptions imageOptions;

    public ImageCatcher(Context context, ImageLoadInfo imageLoadInfo) {
        mFileCache = imageLoadInfo.vinciOptions.getFileCache();
        mMemoryCache = imageLoadInfo.vinciOptions.getMemoryCache();
        imageOptions = imageLoadInfo.imageOptions;
        this.context = context;
    }

    public Bitmap catchImageSync(String mType, int width, int height, String url, DownLoadProgressListener loadListener) {
        MLog.d("ImageCatcher type=" + mType + " url=" + url);
        Bitmap mBitmap = null;
        try {
            Catcher catcher = null;
            switch (mType) {
                case CatcherType.CACHE:
                    catcher = new CacheCatcher(mMemoryCache);
                    mBitmap = (Bitmap) catcher.get(ImageUtils.getCacheKey(url, width, height));
                    break;

                case CatcherType.NET:
                    catcher = new NetCatcher(mFileCache, loadListener);

                    if (catcher.get(url) != null) {
                        catcher.close();
                        catcher = new SDCardCatcher(mFileCache, height, width, imageOptions.isShowOriginal());
                        mBitmap = (Bitmap) catcher.get(url);

                        //remove image if not use file cache
                        if (!imageOptions.isUseFileCache()) {
                            mFileCache.removeDiskFile(url);
                        }
                    } else {
                        catcher.close();
                    }
                    break;

                case CatcherType.SDCARD:
                    catcher = new SDCardCatcher(mFileCache, height, width, imageOptions.isShowOriginal());
                    mBitmap = (Bitmap) catcher.get(url);
                    break;

                case CatcherType.RESOURCE:
                    catcher = new ResourceCatcher(context.getResources(), height, width, imageOptions.isShowOriginal());
                    mBitmap = (Bitmap) catcher.get(url);
                    break;

                case CatcherType.PROVIDER:
                    catcher = new ProviderCatcher();
                    mBitmap = (Bitmap) catcher.get(url);
                    break;

                case CatcherType.ASSET:
                    catcher = new AssetCatcher(context.getResources(), height, width, imageOptions.isShowOriginal());
                    mBitmap = (Bitmap) catcher.get(url);
                    break;

                default:
                    break;
            }

            if (catcher != null) {
                catcher.close();
            }
        } catch (VinciException ve) {
            MLog.e(ve.getMessage());
        }

        if (mBitmap != null && !mBitmap.isRecycled() && imageOptions.isUseMemoryCache()) {
            mMemoryCache.put(ImageUtils.getCacheKey(url, width, height), mBitmap);
        }

        return mBitmap;
    }
}
