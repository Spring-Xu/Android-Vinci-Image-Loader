package com.vinci.imageloader.core.imagecatcher.impl;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.vinci.imageloader.core.exception.VinciException;
import com.vinci.imageloader.core.imagecatcher.Catcher;
import com.vinci.imageloader.core.imagecatcher.CatcherType;

/**
 * Created by SpringXu-Git on 6/3/16.
 */
public class ResourceCatcher implements Catcher<Bitmap> {
    private Resources resources;
    private int imgH;
    private int imgW;
    private boolean showOriginal;

    public ResourceCatcher(Resources resources, int imgH, int imgW, boolean showOriginal) {
        this.resources = resources;
        this.imgH = imgH;
        this.imgW = imgW;
        this.showOriginal = showOriginal;
    }

    @Override
    public Bitmap get(String urlKey) throws VinciException {
        int drawableId = 0;
        try {
            drawableId = Integer.parseInt(urlKey);
        } catch (Exception v) {
            throw new VinciException(v.getMessage());
        }

        if (drawableId == 0) {
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();

        //preload set inJustDecodeBounds true, this will load bitmap into memory
        options.inJustDecodeBounds = true;
        //vinciOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//default is Bitmap.Config.ARGB_8888
        Bitmap bitmap = BitmapFactory.decodeResource(resources, drawableId, options);
        if (bitmap != null) {
            bitmap.recycle();
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

        bitmap = BitmapFactory.decodeResource(resources, drawableId, options);
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

    @Override
    public String getType() {
        return CatcherType.RESOURCE;
    }

    @Override
    public void close() throws VinciException {

    }
}
