package com.vinci.imageloader.core.thread.impl;

/**
 * Created by SpringXu on 15/8/31.
 */
interface Worker {
    void work();

    boolean hasNext();

    void preWork();

    void afterWork();

    boolean isRun();

    Runnable currentWork();

    /**
     * Setter the max run time for a runnable in millis.
     *
     * @param maxRunTime
     */
    void setMaxRunTime(long maxRunTime);

    /**
     * If a runnable runtime is larger than {@link #setMaxRunTime(long)}, we should call {@link #quit()}.
     *
     * @return
     */
    boolean isOutOfRunTime();

    /**
     * Quite current worker.
     */
    void quit();
}
