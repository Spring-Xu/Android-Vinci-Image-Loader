package com.vinci.imageloader.core.thread;

import com.vinci.imageloader.core.consts.DefaultConfig;
import com.vinci.imageloader.core.thread.impl.CustomAsyncTask;
import com.vinci.imageloader.core.thread.impl.ExecutorOptions;

import java.util.concurrent.Executor;

/**
 * Custom executor for Threads, it content executors for {@link Runnable} and {@link CustomAsyncTask}
 */
public abstract class CustomExecutor implements Executor {

    /**
     * max pool size，max execute threads count at the same time
     */
    protected ExecutorOptions options;

    protected CustomExecutor(){
        options = new ExecutorOptions.Builder()
                .setMaxExecutingSize(DefaultConfig.ExecutorPoolDefaultOptions.DEFAULT_MAX_EXECUTING_SIZE)
                .setMaxPoolSize(DefaultConfig.ExecutorPoolDefaultOptions.DEFAULT_MAX_POOL_SIZE)
                .setMaxRunTime(DefaultConfig.ExecutorPoolDefaultOptions.DEFAULT_MAX_RUN_TIME)
                .build();
        lazyInit();
    }

    /**
     * initialize the thread pool and params
     */
    protected void lazyInit() {
        if (options.getMaxPoolSize() <= 0) {
            throw new RuntimeException("FILOThreadPoolExecutor vinciOptions.getMaxPoolSize() is illegal:" + options.getMaxPoolSize());
        }

        if (options.getMaxExecutingSize() <= 0) {
            throw new RuntimeException("FILOThreadPoolExecutor vinciOptions.getMaxExecutingSize() is illegal:" + options.getMaxExecutingSize());
        }
    }


    /**
     * Execute the runnable
     *
     * @param command this task content
     */
    @Override
    public abstract void execute(Runnable command);

    /**
     * shout the thread pool
     */
    public abstract void shoutDown();

    /**
     * If the runable is in queue ,remove it and return true, else if the runable is run, return false, do nothing
     *
     * @param runnable
     */
    public abstract boolean cancel(Runnable runnable);

    /**
     * set vinciOptions
     *
     * @param options
     */
    public void setOptions(ExecutorOptions options) {
        this.options = options;
        lazyInit();
    }
}
