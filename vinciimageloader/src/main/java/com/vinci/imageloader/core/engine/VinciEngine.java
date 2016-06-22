package com.vinci.imageloader.core.engine;

import android.os.Handler;
import android.os.Looper;

import com.vinci.imageloader.core.bridge.BridgeFactory;
import com.vinci.imageloader.core.consts.Const;
import com.vinci.imageloader.core.consts.DefaultConfig;
import com.vinci.imageloader.core.thread.CustomExecutor;
import com.vinci.imageloader.core.thread.impl.ExecutorOptions;
import com.vinci.imageloader.core.view.VinciView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by SpringXu on 6/16/16.
 */
public class VinciEngine {
    /**
     * Cache the image id and the id meets Uri.
     */
    private Map<Integer, String> memoryCacheKey = Collections.synchronizedMap(new HashMap<Integer, String>());

    /**
     * Cache the locker for the load Uri, one Uri meets one locker, this is use to keep one Uri in load state at the same time.
     */
    private Map<String, ReentrantLock> cacheLocks = Collections.synchronizedMap(new HashMap<String, ReentrantLock>());

    private CustomExecutor executor;

    private Handler mainHandler;
    private volatile long latestPostTime;
    private final int DELAY_TIME = 50;

    public VinciEngine(CustomExecutor executor) {
        this.executor = executor;
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public void prepareToShowImage(VinciView vinciView, String cacheKey) {
        memoryCacheKey.put(vinciView.getId(), cacheKey);
    }

    public String getCacheKey(VinciView vinciView) {
        return memoryCacheKey.get(vinciView.getId());
    }

    public void removeCacheKey(VinciView vinciView) {
        memoryCacheKey.remove(vinciView.getId());
    }

    public Lock getUniqueLock(String uri) {
        ReentrantLock uniqueLock = null;
        uniqueLock = cacheLocks.get(uri);
        if (uniqueLock == null) {
            uniqueLock = new ReentrantLock();
            cacheLocks.put(uri, uniqueLock);
        }

        return uniqueLock;
    }

    public void releaseLock(String uri) {
        cacheLocks.remove(uri);
    }

    public void execute(boolean inMainThread, boolean isSync, Runnable runnable) {
        lazyInit();

        if (inMainThread) {
            // every time , the time delay 20 mills.
            long delayTime = DELAY_TIME;
            if (latestPostTime == 0) {
                latestPostTime = System.currentTimeMillis() + delayTime;

            } else {
                delayTime = (System.currentTimeMillis() - latestPostTime) > 0 ? DELAY_TIME : (latestPostTime - System.currentTimeMillis()) + DELAY_TIME;
                latestPostTime = System.currentTimeMillis() + delayTime;
            }
            mainHandler.postDelayed(runnable, delayTime);

        } else if (isSync) {

            runnable.run();
        } else {
            executor.execute(runnable);
        }
    }

    protected void lazyInit() {
        if (executor == null) {
            CustomExecutor executor = BridgeFactory.createExecutor(Const.ExecutorPoolType.FIFOPOOL);
            ExecutorOptions executorOptions = new ExecutorOptions.Builder()
                    .setMaxExecutingSize(DefaultConfig.ExecutorPoolDefaultOptions.DEFAULT_MAX_EXECUTING_SIZE)
                    .setMaxPoolSize(DefaultConfig.ExecutorPoolDefaultOptions.DEFAULT_MAX_POOL_SIZE)
                    .setMaxRunTime(DefaultConfig.ExecutorPoolDefaultOptions.DEFAULT_MAX_RUN_TIME)
                    .build();
            executor.setOptions(executorOptions);
        }

        if (mainHandler == null) {
            mainHandler = new Handler(Looper.getMainLooper());
        }
    }
}
