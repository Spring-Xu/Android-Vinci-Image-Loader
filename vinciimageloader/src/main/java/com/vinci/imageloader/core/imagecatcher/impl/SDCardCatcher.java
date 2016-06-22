package com.vinci.imageloader.core.imagecatcher.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.vinci.imageloader.core.exception.VinciException;
import com.vinci.imageloader.core.filecache.FileCache;
import com.vinci.imageloader.core.imagecatcher.Catcher;
import com.vinci.imageloader.core.imagecatcher.CatcherType;
import com.vinci.imageloader.core.util.ImageUtils;
import com.vinci.imageloader.core.util.MLog;

/**
 * Created by SpringXu-Git on 15/11/10.
 */
public class SDCardCatcher implements Catcher<Bitmap> {
    private int imgH;
    private int imgW;
    private FileCache mFileCache;
    private boolean showOriginal;

    public SDCardCatcher(FileCache fileCache, int imgHeight, int imgWidth, boolean showOriginal) {
        this.imgH = imgHeight;
        this.imgW = imgWidth;
        this.mFileCache = fileCache;
        this.showOriginal = showOriginal;
    }

    @Override
    public Bitmap get(String urlKey) throws VinciException {
        Bitmap bitmap = null;
        try {
            String filePath = mFileCache.getFilePath(urlKey);

            BitmapFactory.Options options = new BitmapFactory.Options();

            //preload set inJustDecodeBounds true, this will load bitmap into memory
            options.inJustDecodeBounds = true;
            //vinciOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//default is Bitmap.Config.ARGB_8888
            bitmap = BitmapFactory.decodeFile(filePath, options);
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

            int orientation = ImageUtils.getExifOrientation(filePath);

            bitmap = BitmapFactory.decodeFile(filePath, options);

            bitmap = ImageUtils.rotateBitmap(bitmap, orientation);

        } catch (Exception e) {
            MLog.d("SDCardCatcher load image urlKey = " + urlKey + " error:" + e.getMessage());
            throw new VinciException(e.getMessage());
        } catch (OutOfMemoryError oom) {
            throw new VinciException(oom.getMessage());
        }

        return bitmap;
    }

    @Override
    public String getType() {
        return CatcherType.SDCARD;
    }

    @Override
    public void close() throws VinciException {

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
}
