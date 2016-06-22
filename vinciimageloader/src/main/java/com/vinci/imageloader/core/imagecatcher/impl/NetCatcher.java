package com.vinci.imageloader.core.imagecatcher.impl;

import com.vinci.imageloader.core.exception.VinciException;
import com.vinci.imageloader.core.filecache.DownLoadProgressListener;
import com.vinci.imageloader.core.filecache.FileCache;
import com.vinci.imageloader.core.imagecatcher.Catcher;
import com.vinci.imageloader.core.imagecatcher.CatcherType;
import com.vinci.imageloader.core.util.MLog;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Result is file path.
 * Created by SpringXu-Git on 15/11/10.
 */
public class NetCatcher implements Catcher<String> {

    private final String TAG = NetCatcher.class.getSimpleName();
    //download control
    private InputStream input = null;
    private HttpURLConnection connection = null;
    private FileCache mFileCache;
    private DownLoadProgressListener mLoadListener;

    public NetCatcher(FileCache fileCache, DownLoadProgressListener loadListener) {
        this.mFileCache = fileCache;
        this.mLoadListener = loadListener;
    }

    @Override
    public String get(String urlKey) throws VinciException {
        try {
            URL mUrl = new URL(urlKey);
            connection = (HttpURLConnection) mUrl.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                MLog.e(TAG, "downloadImage fail getResponseCode:" + connection.getResponseCode());
                if (mFileCache != null) {
                    mFileCache.removeDiskFile(urlKey);
                }

                throw new VinciException("downloadImage fail getResponseCode:" + connection.getResponseCode());
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            input = connection.getInputStream();

            if (mFileCache != null) {
                mFileCache.addDiskFile(urlKey, new FlushedInputStream(input), mLoadListener, fileLength);

            } else {
                MLog.e("NetCatcher save file failed, because file cache is null");
            }

        } catch (Throwable t) {
            t.printStackTrace();
            if (mFileCache != null) {
                mFileCache.removeDiskFile(urlKey);
            }

            throw new VinciException(t.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.disconnect();
                }

                if (input != null) {
                    input.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (mFileCache != null) {
                    mFileCache.removeDiskFile(urlKey);
                }

                throw new VinciException(e.getMessage());
            }
        }

        if (mFileCache != null) {
            return mFileCache.getFilePath(urlKey);
        } else {
            return null;
        }
    }

    @Override
    public String getType() {
        return CatcherType.NET;
    }

    @Override
    public void close() throws VinciException {
        try {
            if (connection != null) {
                connection.disconnect();
            }

            if (input != null) {
                input.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new VinciException(e.getMessage());
        }
    }

    /**
     * Work for download image, when the net state is bad, this will tell the http where to stop download file.
     */
    public class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int b = read();
                    if (b < 0) {
                        break;  // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }
}
