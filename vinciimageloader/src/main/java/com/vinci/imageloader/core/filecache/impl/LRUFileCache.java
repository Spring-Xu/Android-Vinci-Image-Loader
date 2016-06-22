package com.vinci.imageloader.core.filecache.impl;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.vinci.imageloader.core.consts.DefaultConfig;
import com.vinci.imageloader.core.filecache.DownLoadProgressListener;
import com.vinci.imageloader.core.filecache.FileCache;
import com.vinci.imageloader.core.thread.impl.CustomAsyncTask;
import com.vinci.imageloader.core.util.MD5Util;
import com.vinci.imageloader.core.util.MLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SpringXu on 15/8/24.
 */
public class LRUFileCache implements FileCache {

    private static String TAG = "LRUFileCache";

    /**
     * cache config
     */
    private FileCacheOptions options;
    /**
     * cache file suffix
     */
    private static final String WHOLESALE_CONV = ".cach";
    /**
     * mini free space on SDCard
     */
    private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10 * 1024 * 1024;

    /**
     * the key is file url or path, value is true is the file exist or the file is not exist
     */
    private Map<String, Boolean> fileCache;

    /**
     * Progress call back interval time
     */
    private final int INTERVAL_TIME = 400;

    /**
     * use default vinciOptions
     */
    public LRUFileCache() {
        this.options = new FileCacheOptions.Builder()
                .setCacheRootPath(DefaultConfig.FileCacheDefaultOptions.ROOT_PATH)
                .setIsUseFileCache(DefaultConfig.FileCacheDefaultOptions.USER_FILE_CACHE)
                .setMaxCacheSize(DefaultConfig.FileCacheDefaultOptions.MAX_CACHE_SIZE)
                .setMaxFileCount(DefaultConfig.FileCacheDefaultOptions.MAX_FILE_COUNT)
                .builder();
        init();
    }

    public LRUFileCache(FileCacheOptions options) {
        this.options = options;
        init();
    }

    public void setOptions(FileCacheOptions options) {
        this.options = options;
        init();
    }

    private void init() {
        fileCache = new HashMap<String, Boolean>();
        CustomAsyncTask<Void, Void, Map<String, Boolean>> task = new CustomAsyncTask<Void, Void, Map<String, Boolean>>() {
            @Override
            protected Map<String, Boolean> doInBackground(Void... params) {
                Map<String, Boolean> map = new HashMap<String, Boolean>();

                if (!new File(options.getCacheRootPath()).exists()) {
                    return map;
                }

                File[] files = new File(options.getCacheRootPath()).listFiles();
                for (File file : files) {
                    map.put(file.getPath(), true);
                }
                return map;
            }

            @Override
            protected void onPostExecute(Map<String, Boolean> result) {
                super.onPostExecute(result);
                fileCache = result;
            }
        };

        task.execute();
    }

    @Override
    public void addDiskFile(String key, InputStream inputStream, DownLoadProgressListener loadListener, int totalLength) {
        if (TextUtils.isEmpty(key) || inputStream == null) {
            return;
        }

        testLog("addDiskFile:" + key);

        String filename = convertUrlToFileName(key);
        String dir = options.getCacheRootPath();
        File dirFile = new File(dir);
        if (!dirFile.exists())
            dirFile.mkdirs();
        File file = new File(dir + "/" + filename);
        OutputStream outStream;

        try {
            if (file.exists()) {
                file.delete();
            }

            file.createNewFile();
            outStream = new FileOutputStream(file);
            //four buffer area
            byte bytes1024[] = new byte[1024];
            byte bytes128[] = new byte[128];
            byte bytes32[] = new byte[32];
            int i = 0;

            int length = 0;
            long timeStamp = System.currentTimeMillis();

            do {
                if (inputStream.available() > 1024) {
                    inputStream.read(bytes1024);
                    outStream.write(bytes1024);
                    length += 1024;
                    if (loadListener != null && isShowTime(timeStamp)) {
                        timeStamp = System.currentTimeMillis();
                        showSpeed(loadListener, key, length, totalLength);
                    }
                    continue;
                }

                if (inputStream.available() > 128) {
                    inputStream.read(bytes128);
                    outStream.write(bytes128);
                    length += 128;
                    if (loadListener != null && isShowTime(timeStamp)) {
                        timeStamp = System.currentTimeMillis();
                        showSpeed(loadListener, key, length, totalLength);
                    }
                    continue;
                }

                if (inputStream.available() > 32) {
                    inputStream.read(bytes32);
                    outStream.write(bytes32);
                    length += 32;
                    if (loadListener != null && isShowTime(timeStamp)) {
                        timeStamp = System.currentTimeMillis();
                        showSpeed(loadListener, key, length, totalLength);
                    }
                    continue;
                }

                i = inputStream.read();
                if (i != -1) {
                    outStream.write(i);

                } else {
                    break;
                }

            } while (true);

            outStream.flush();
            outStream.close();
            inputStream.close();

            fileCache.put(file.getPath(), true);
        } catch (Throwable e) {
            testLog(e.getMessage());
            file.delete();
        }

        // free the space at every time to add a new file
        freeSpaceIfNeeded();
    }

    /**
     * Show the image download speed in main thread
     *
     * @param loadListener
     * @param url
     * @param currSize
     * @param totalLength
     */
    private void showSpeed(final DownLoadProgressListener loadListener, final String url, final int currSize, final int totalLength) {
        if (loadListener != null) {
            loadListener.onDownloadSpeed(url, currSize, totalLength);
        }
    }

    /**
     * If the time is larger than 600 mills, show the speed once
     *
     * @param timeStamp
     * @return
     */
    private boolean isShowTime(long timeStamp) {
        return System.currentTimeMillis() - timeStamp > INTERVAL_TIME;
    }

    @Override
    public File getDiskFile(String key) {
        File file = new File(getFilePath(key));

        testLog("getDiskFile:" + key);

        if (file != null && file.exists()) {
            updateFileTime(file);
            testLog("getDiskFile find");

        } else {
            file = null;
        }

        return file;
    }

    @Override
    public boolean isExist(String key) {
        String path = getFilePath(key);
        Boolean result = fileCache.get(path);
        if (fileCache.isEmpty()) {
            return new File(path).exists();
        }

        testLog("fileCache:" + fileCache.size());
        return result == null ? false : result.booleanValue();
    }

    @Override
    public void removeDiskFile(String key) {
        testLog("removeDiskFile key:" + key);

        File file = getDiskFile(key);
        if (file != null && file.exists()) {
            fileCache.remove(file.getPath());
            file.delete();
        }
    }

    @Override
    public void removeAllDiskFiles() {
        new File(options.getCacheRootPath()).delete();
        fileCache.clear();
    }

    @Override
    public void shutDown() {
        fileCache.clear();
    }

    /**
     * This method will free the files which had not been used at a long time
     */
    private void freeSpaceIfNeeded() {
        File dir = new File(options.getCacheRootPath());
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }

        int dirSize = 0;
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().contains(WHOLESALE_CONV)) {
                dirSize += files[i].length();
            }
        }
        // if the dir size larger than max size or the free space on SDCard is less than 10MB
        //free 40% space for system
        if (dirSize > options.getMaxCacheSize()
                || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            // delete 40% files by LRU
            int removeFactor = (int) ((0.4 * files.length) + 1);
            // sort the files by modify time
            Arrays.sort(files, new FileLastModifSort());
            // delete files
            for (int i = 0; i < removeFactor; i++) {
                if (files[i].getName().contains(WHOLESALE_CONV)) {
                    files[i].delete();
                    fileCache.remove(files[i].getPath());
                }
            }
        }

        //if file count is larger than max count, delete the last
        if (files.length > options.getMaxFileCount()) {
            Arrays.sort(files, new FileLastModifSort());
            // delete files
            for (int i = 0; i < files.length - options.getMaxFileCount(); i++) {
                if (files[i].getName().contains(WHOLESALE_CONV)) {
                    files[i].delete();
                    fileCache.remove(files[i].getPath());
                }
            }
        }

        testLog("fileCache:" + fileCache.size());
    }

    /**
     * Modify the file time
     *
     * @param file the file which need to update time
     */
    public void updateFileTime(File file) {
        if (file != null && file.exists()) {
            long newModifiedTime = System.currentTimeMillis();
            file.setLastModified(newModifiedTime);
        }
    }

    /**
     * get the free space on SDCard
     *
     * @return free size in MB
     */
    private int freeSpaceOnSd() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
                .getPath());
        double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
                .getBlockSize());
        return (int) sdFreeMB;
    }

    /**
     * Get the file name by file url
     *
     * @param url
     * @return file name
     */
    private String convertUrlToFileName(String url) {
        return MD5Util.getMD5Values(url) + WHOLESALE_CONV;
    }

    @Override
    public String getFilePath(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;

        } else if (new File(key).exists()) {
            return key;

        } else if (URLUtil.isNetworkUrl(key)) {
            return options.getCacheRootPath() + "/" + convertUrlToFileName(key);

        } else {
            return null;
        }
    }

    /**
     * The comparator for the file modify, sort the files by modify time.
     */
    private class FileLastModifSort implements Comparator<File> {
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }

    }

    private final boolean ISDEBUG = false;

    private void testLog(String str) {
        if (ISDEBUG) {
            MLog.e(TAG, str);
        }
    }
}
