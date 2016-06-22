package com.vinci.imageloader.core.engine;

import android.graphics.Bitmap;

import com.vinci.imageloader.core.filecache.DownLoadProgressListener;
import com.vinci.imageloader.core.imagecatcher.CatcherType;
import com.vinci.imageloader.core.util.ImageUtils;
import com.vinci.imageloader.core.util.MLog;
import com.vinci.imageloader.core.view.VinciView;

import java.util.concurrent.locks.Lock;

/**
 * Created by SpringXu on 6/16/16.
 */
public class ImageLoadTask implements Runnable, DownLoadProgressListener {
    private final String TAG = ImageLoadTask.class.getSimpleName();

    private final int TYPE_CANCEL = 1;
    private final int TYPE_ERROR = 2;
    private final int TYPE_START = 3;
    private final int TYPE_NONE = 4;
    private final int TYPE_PROGRESS = 5;
    private final int TYPE_OK = 6;

    private ImageLoadInfo imageLoadInfo;
    private VinciEngine vinciEngine;
    private VinciView vinciView;

    public ImageLoadTask(ImageLoadInfo imageLoadInfo, VinciView vinciView, VinciEngine engine) {
        this.vinciEngine = engine;
        this.imageLoadInfo = imageLoadInfo;
        this.vinciView = vinciView;
    }

    @Override
    public void run() {
        Lock lock = vinciEngine.getUniqueLock(imageLoadInfo.mUri);
        lock.lock();
        MLog.d(TAG, "lock uri=" + imageLoadInfo.mUri);
        try {
            noticeLoadState(TYPE_START, null);

            if (isCancelNecessary()) {
                MLog.d(TAG, "isCancelNecessary 1" + " uri=" + imageLoadInfo.mUri);
                noticeLoadState(TYPE_CANCEL, "load canceled...");
                return;
            }

            MLog.d(TAG, "load from memory" + " uri=" + imageLoadInfo.mUri);
            Bitmap bitmap = (Bitmap) imageLoadInfo.vinciOptions.getMemoryCache().get(vinciView.getViewKey());
            if (bitmap != null && !bitmap.isRecycled()) {
                noticeLoadStateOK(TYPE_OK, bitmap);
                return;
            }

            MLog.d(TAG, "hunt image" + " uri=" + imageLoadInfo.mUri);
            ImageCatcher imageHunter = new ImageCatcher(vinciView.getWapView().getContext(), imageLoadInfo);
            bitmap = catchBitmap(imageHunter);

            if (isCancelNecessary()) {
                MLog.d(TAG, "isCancelNecessary 2" + " uri=" + imageLoadInfo.mUri);
                noticeLoadState(TYPE_CANCEL, "load canceled...");
                return;
            }

            //deal with result
            if (bitmap == null || bitmap.isRecycled() || bitmap.getWidth() == 0 || bitmap.getHeight() == 0) {
                noticeLoadState(TYPE_NONE, "bitmap result is null");

            } else {
                noticeLoadStateOK(TYPE_OK, bitmap);
            }

        } catch (Exception e) {
            MLog.e("ImageLoadTask", e.getMessage());
            noticeLoadState(TYPE_ERROR, e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    protected boolean isCancelNecessary() {
        return isInterrupted() || isViewReleased() || isViewReused();
    }

    private boolean isViewReleased() {
        return vinciView.getWapView() == null;
    }

    private boolean isViewReused() {
        if (!vinciView.getViewKey().equals(vinciEngine.getCacheKey(vinciView))) {
            MLog.d(TAG, "isViewReused");
            return true;
        }

        return false;
    }

    private boolean isInterrupted() {
        if (Thread.currentThread().isInterrupted()) {
            MLog.d(TAG, "isInterrupted");
            return true;
        } else {
            return false;
        }
    }

    private String getLoadType(String uri) {
        String type = ImageUtils.getCatchTypeByUri(uri);
        String url = ImageUtils.getCatchUrlByUri(uri);
        // get load type
        String getType = null;
        if (type.equals(CatcherType.NET)) {
            if (imageLoadInfo.vinciOptions.getFileCache().isExist(url)) {
                getType = CatcherType.SDCARD;

            } else {
                getType = CatcherType.NET;
            }

        } else {
            getType = type;
        }

        return getType;
    }

    private void noticeLoadStateProgress(int type, int nCurSize, int nTotal) {
        StateNoticer stateNoticer = new StateNoticer(type, null, nTotal, nCurSize, null);
        vinciEngine.execute(true, false, stateNoticer);
    }

    private void noticeLoadStateOK(int type, Bitmap bitmap) {
        StateNoticer stateNoticer = new StateNoticer(type, null, 0, 0, bitmap);
        vinciEngine.execute(true, false, stateNoticer);
    }

    private void noticeLoadState(int type, String errorMsg) {
        MLog.d(TAG, "noticeLoadState:" + errorMsg + " url=" + imageLoadInfo.mUri);
        StateNoticer stateNoticer = new StateNoticer(type, errorMsg, 0, 0, null);
        vinciEngine.execute(true, false, stateNoticer);
    }

    protected Bitmap catchBitmap(ImageCatcher imageHunter) {
        String hunterType = getLoadType(imageLoadInfo.mUri);
        String url = ImageUtils.getCatchUrlByUri(imageLoadInfo.mUri);

        return imageHunter.catchImageSync(hunterType, vinciView.getWidth(), vinciView.getHeight(), url, this);
    }

    @Override
    public void onDownloadSpeed(String url, int nCurSize, int nTotal) {
        noticeLoadStateProgress(TYPE_PROGRESS, nCurSize, nTotal);
    }

    private class StateNoticer implements Runnable {
        private int type;
        private Bitmap bitmap;
        private String errorMsg;
        private int maxLength;
        private int currentLength;

        public StateNoticer(int type, String errorMsg, int maxLength, int currentLength, Bitmap bitmap) {
            this.type = type;
            this.bitmap = bitmap;
            this.errorMsg = errorMsg;
            this.maxLength = maxLength;
            this.currentLength = currentLength;
        }

        @Override
        public void run() {
            if (imageLoadInfo == null || imageLoadInfo.listener == null) {
                return;
            }

            boolean shouldShowImage = vinciView.getViewKey().equals(vinciEngine.getCacheKey(vinciView));
            switch (type) {
                case TYPE_CANCEL:
                    if (shouldShowImage) {
                        if (imageLoadInfo.imageOptions.shouldShowErrorImage()) {
                            setImageResource(vinciView, imageLoadInfo.imageOptions.getErrorImageResId());
                        }
                    }
                    imageLoadInfo.listener.onDownloadFail(errorMsg, imageLoadInfo.mUri);
                    break;

                case TYPE_START:
                    if (shouldShowImage) {
                        if (imageLoadInfo.imageOptions.shouldShowLoadingIamge()) {
                            setImageResource(vinciView, imageLoadInfo.imageOptions.getLoadingImageResId());
                        }
                    }
                    break;

                case TYPE_ERROR:
                    if (shouldShowImage) {
                        if (imageLoadInfo.imageOptions.shouldShowErrorImage()) {
                            setImageResource(vinciView, imageLoadInfo.imageOptions.getErrorImageResId());
                        }
                    }
                    imageLoadInfo.listener.onDownloadFail(errorMsg, imageLoadInfo.mUri);
                    break;

                case TYPE_NONE:
                    if (shouldShowImage) {
                        if (imageLoadInfo.imageOptions.shouldShowErrorImage()) {
                            setImageResource(vinciView, imageLoadInfo.imageOptions.getErrorImageResId());
                        }
                    }
                    imageLoadInfo.listener.onDownloadFail(errorMsg, imageLoadInfo.mUri);
                    break;

                case TYPE_PROGRESS:
                    imageLoadInfo.listener.onDownloadSpeed(imageLoadInfo.mUri, currentLength, maxLength);
                    break;

                case TYPE_OK:
                    //display image
                    if (shouldShowImage) {
                        vinciView.setBitmap(bitmap);
                    }
                    imageLoadInfo.listener.onDownloadOK(imageLoadInfo.mUri, bitmap);
                    break;

                default:
                    break;
            }
        }
    }

    public void setImageResource(VinciView vinciView, int resId) {
        String url = String.valueOf(resId);
        String cacheKey = ImageUtils.getCacheKey(url, vinciView.getWidth(), vinciView.getHeight());
        Bitmap bitmap = (Bitmap) imageLoadInfo.vinciOptions.getMemoryCache().get(cacheKey);

        if (bitmap == null || bitmap.isRecycled()) {
            ImageCatcher imageHunter = new ImageCatcher(vinciView.getWapView().getContext(), imageLoadInfo);
            bitmap = imageHunter.catchImageSync(CatcherType.RESOURCE, vinciView.getWidth(), vinciView.getHeight(),
                    String.valueOf(resId), null);
        }

        if (bitmap != null && !bitmap.isRecycled()) {
            vinciView.setBitmap(bitmap);
        }
    }
}
