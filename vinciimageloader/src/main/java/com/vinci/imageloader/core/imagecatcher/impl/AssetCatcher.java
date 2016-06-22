package com.vinci.imageloader.core.imagecatcher.impl;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.vinci.imageloader.core.exception.VinciException;
import com.vinci.imageloader.core.imagecatcher.Catcher;
import com.vinci.imageloader.core.imagecatcher.CatcherType;

import java.io.InputStream;

/**
 * Created by SpringXu-Git on 6/3/16.
 */
public class AssetCatcher implements Catcher<Bitmap> {
    private Resources resources;
    private int imgH;
    private int imgW;
    private boolean showOriginal;

    public AssetCatcher(Resources resources, int imgH, int imgW, boolean showOriginal) {
        this.resources = resources;
        this.imgH = imgH;
        this.imgW = imgW;
    }

    @Override
    public Bitmap get(String urlKey) throws VinciException {
        InputStream inputStream = null;
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();

            //preload set inJustDecodeBounds true, this will load bitmap into memory
            options.inJustDecodeBounds = true;
            //vinciOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//default is Bitmap.Config.ARGB_8888
            inputStream = resources.getAssets().open(urlKey);
            bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            if (bitmap != null) {
                bitmap.recycle();
                bitmap = null;
            }

            //get the image information include: height and width
            int height = options.outHeight;
            int width = options.outWidth;
            //get sample size
            int sampleSize = 1;
            if (!showOriginal) {
                sampleSize = getScaleInSampleSize(width, height, imgW, imgH);
            }
            options.inSampleSize = sampleSize;
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;

            bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        } catch (Exception e) {
            throw new VinciException(e.getMessage());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                //do nothing
            }
        }

        return bitmap;
    }

    /**
     * get the scale sample size
     *
     * @param resW resource width
     * @param resH resource height
     * @param desW result width
     * @param desH result height
     * @return
     */
    public int getScaleInSampleSize(int resW, int resH, int desW, int desH) {
        if (desH == 0 || desW == 0) {
            return 1;//default size
        }

        int scaleW = resW / desW;
        int scaleH = resH / desH;
        int largeScale = scaleH > scaleW ? scaleH : scaleW;

        int sampleSize = 1;
        while (sampleSize < largeScale) {
            sampleSize *= 2;
        }

        return sampleSize;
    }

    /**
     * get the image size in the RAM
     *
     * @param imageW
     * @param imageH
     * @return
     */
    public long getBitmapSizeInMemory(int imageW, int imageH) {
        return imageH * imageW * getBytesPerPixel(Bitmap.Config.ARGB_8888);
    }

    /**
     * A helper function to return the byte usage per pixel of a bitmap based on its configuration.
     */
    public int getBytesPerPixel(Bitmap.Config config) {
        if (config == Bitmap.Config.ARGB_8888) {
            return 4;
        } else if (config == Bitmap.Config.RGB_565) {
            return 2;
        } else if (config == Bitmap.Config.ARGB_4444) {
            return 2;
        } else if (config == Bitmap.Config.ALPHA_8) {
            return 1;
        }
        return 1;
    }


    @Override
    public String getType() {
        return CatcherType.ASSET;
    }

    @Override
    public void close() throws VinciException {

    }
}
