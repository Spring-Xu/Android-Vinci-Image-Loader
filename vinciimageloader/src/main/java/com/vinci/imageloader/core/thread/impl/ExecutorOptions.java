package com.vinci.imageloader.core.thread.impl;

import com.vinci.imageloader.core.consts.DefaultConfig;

/**
 * Created by SpringXu on 15/9/16.
 */
public class ExecutorOptions {
    private int maxExecutingSize;

    private int maxPoolSize;

    private long maxRunTime;

    private ExecutorOptions(Builder builder) {
        this.maxExecutingSize = builder.maxExecutingSize;
        this.maxPoolSize = builder.maxPoolSize;
        this.maxRunTime = builder.maxRunTime;
    }

    public long getMaxRunTime() {
        return maxRunTime;
    }

    public void setMaxRunTime(long maxRunTime) {
        this.maxRunTime = maxRunTime;
    }

    /**
     * get the max executing size, the executing size is what how many thread can be create at the same time
     *
     * @return
     */
    public int getMaxExecutingSize() {
        return maxExecutingSize;
    }

    /**
     * the pool size is the size in the queue which have not been execute
     *
     * @return
     */
    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxExecutingSize(int maxExecutingSize) {
        this.maxExecutingSize = maxExecutingSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public static class Builder {
        private int maxExecutingSize = DefaultConfig.ExecutorPoolDefaultOptions.DEFAULT_MAX_EXECUTING_SIZE;

        private int maxPoolSize = DefaultConfig.ExecutorPoolDefaultOptions.DEFAULT_MAX_POOL_SIZE;

        private long maxRunTime = DefaultConfig.ExecutorPoolDefaultOptions.DEFAULT_MAX_RUN_TIME;

        /**
         * Setter the max run time of a thread in millis.
         *
         * @param maxRunTime
         * @return
         */
        public Builder setMaxRunTime(long maxRunTime) {
            this.maxRunTime = maxRunTime;
            return this;
        }

        /**
         * set the max executing size, the executing size is what how many thread can be create at the same time
         *
         * @param maxExecutingSize
         */
        public Builder setMaxExecutingSize(int maxExecutingSize) {
            this.maxExecutingSize = maxExecutingSize;
            return this;
        }

        /**
         * set the pool size is the size in the queue which have not been execute
         *
         * @param maxPoolSize
         */
        public Builder setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
            return this;
        }

        public ExecutorOptions build() {
            return new ExecutorOptions(this);
        }
    }
}
