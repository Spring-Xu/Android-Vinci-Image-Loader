package com.vinci.imageloader.load;

import android.graphics.Bitmap;

/**
 * Created by SpringXu on 15/9/3.
 */
public interface ImageLoadListener {

    /**
     * The down load action is start
     *
     * @param url the net url of ths image
     */
    void onDownloadStart(String url);

    /**
     * The down load work is finished.
     *
     * @param url    Image url
     * @param bitmap result bitmap
     */
    void onDownloadOK(String url, Bitmap bitmap);

    /**
     * The image download failed
     *
     * @param errorMsg The reason for Fail
     * @param url      image url
     */
    void onDownloadFail(String errorMsg, String url);

    /**
     * the image download speed
     *
     * @param url      image url
     * @param nCurSize the current size which had been downloaded
     * @param nTotal   the total size of the image
     */
    void onDownloadSpeed(String url, int nCurSize, int nTotal);
}
