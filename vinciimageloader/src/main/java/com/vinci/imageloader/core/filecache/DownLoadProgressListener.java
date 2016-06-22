package com.vinci.imageloader.core.filecache;

/**
 * Created by SpringXu on 6/12/16.
 */
public interface DownLoadProgressListener {
    void onDownloadSpeed(String url, int nCurSize, int nTotal);
}
