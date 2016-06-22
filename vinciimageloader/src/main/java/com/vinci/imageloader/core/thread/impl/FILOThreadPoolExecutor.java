package com.vinci.imageloader.core.thread.impl;

import android.os.Handler;
import android.os.HandlerThread;

import com.vinci.imageloader.core.consts.DefaultConfig;
import com.vinci.imageloader.core.thread.CustomExecutor;
import com.vinci.imageloader.core.util.MLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Base on stack, first in ,last out.
 * Created by SpringXu on 15/8/31.
 */
public class FILOThreadPoolExecutor extends CustomExecutor {

    private static String TAG = FILOThreadPoolExecutor.class.getSimpleName();

    /**
     * use a stack to store jobs
     */
    private Stack<Runnable> mStack;

    private List<Worker> workers;

    /**
     * the thread max interval time at each of them (mills)
     */
//    private int maxInterval;

    /**
     * pool shout down tag
     */
    private boolean isShoutDown = false;

    /**
     * Get the instance which has the default config, you can see {@link DefaultConfig}
     */
    public FILOThreadPoolExecutor() {
        super();
        mStack = new Stack<Runnable>();

        workers = Collections.synchronizedList(new ArrayList<Worker>());
    }

    /**
     * Execute the runnable
     *
     * @param runnable this task content
     */
    @Override
    public void execute(Runnable runnable) {
        if (runnable == null) {
            throw new RuntimeException("FILOThreadPoolExecutor # execute runnable is null");
        }

        if (isShoutDown) {
            throw new RuntimeException("FILOThreadPoolExecutor pool is shout downed");
        }

        if (isPoolFull()) {
            throw new RuntimeException("Executor pool is full");
        }

        offerRunnable(runnable);
    }

    @Override
    public boolean cancel(Runnable runnable) {
        synchronized (mStack) {
            if (mStack.contains(runnable) && mStack.remove(runnable)) {
                MLog.d(TAG, "cancel runable succ");
                return true;
            }
        }

        boolean find = false;
        synchronized (workers) {
            for (Worker worker : workers) {
                if (worker != null && worker.currentWork() == runnable) {
                    find = true;
                    break;
                }
            }
        }


        if (find) {
            MLog.d(TAG, "cancel runable fail");
            return false;

        } else {
            MLog.d(TAG, "cancel runable succ2");
            return true;
        }
    }

    private void offerRunnable(Runnable runnable) {

        if (isExecutingPoolFull()) {
            synchronized (mStack) {
                mStack.push(runnable);
            }

        } else {
            Worker worker = new TaskWorker(runnable);
            worker.setMaxRunTime(options.getMaxRunTime());
            workers.add(worker);
            worker.work();
        }
        MLog.d(TAG, "offerRunnable end");
    }

    /**
     * Get the excuting pool size, if it is larger than {@link #options} return true, else return false
     *
     * @return
     */
    public boolean isExecutingPoolFull() {
        MLog.d(TAG, "isExecutingPoolFull 1");
        synchronized (workers) {
            for (int i = 0; i < workers.size(); ) {
                Worker worker = workers.get(i);
                if (worker == null || !worker.isRun()) {
                    workers.remove(i);

                } else if (worker.isOutOfRunTime()) {
                    worker.quit();
                    workers.remove(i);

                } else {
                    i++;
                }
            }
        }
        MLog.d(TAG, "isExecutingPoolFull 2");

        return workers.size() >= options.getMaxExecutingSize();
    }


    public boolean isPoolFull() {
        synchronized (mStack) {
            return mStack.size() >= options.getMaxPoolSize();
        }
    }

    @Override
    public void shoutDown() {
        isShoutDown = true;
        synchronized (mStack) {
            mStack.clear();
        }
        workers.clear();
    }

    /**
     * worker for runnable or task
     */
    class TaskWorker implements Worker {

        private Runnable runnable;
        private HandlerThread handlerThread;
        private Handler currentHandler;
        /**
         * The latest runnable start run time
         */
        private long startRunTime;

        private long maxRunTime = 100;

        public TaskWorker(Runnable run) {
            runnable = run;
        }

        @Override
        public void afterWork() {
            synchronized (mStack) {
                if (hasNext() && !isOutOfRunTime()) {
                    try {
                        runnable = mStack.pop();
                        workFor(runnable);
                        MLog.d(TAG, "TaskWorker worker for next:" + mStack.size());
                    } catch (Exception i) {
                        i.printStackTrace();
                    }

                } else {
                    // recycle worker
                    workers.remove(this);
                    handlerThread.quit();
                    MLog.d(TAG, "TaskWorker finish");
                }
            }
        }

        @Override
        public void work() {
            if (runnable == null) {
                throw new RuntimeException("runnable is null");
            }

            preWork();
            handlerThread = new HandlerThread("TaskWorker#" + System.currentTimeMillis());
            handlerThread.start();
            currentHandler = new Handler(handlerThread.getLooper());
            workFor(runnable);
        }

        private void workFor(final Runnable run) {
            if (handlerThread == null || !handlerThread.isAlive() || handlerThread.isInterrupted()) {
                handlerThread = new HandlerThread("TaskWorker#" + System.currentTimeMillis());
                handlerThread.start();
                currentHandler = new Handler(handlerThread.getLooper());
            }

            currentHandler.post(new Runnable() {
                @Override
                public void run() {
                    startRunTime = System.currentTimeMillis();
                    run.run();
                    afterWork();
                }
            });
        }

        @Override
        public void preWork() {
        }

        @Override
        public Runnable currentWork() {
            return runnable;
        }

        @Override
        public boolean hasNext() {
            return !mStack.isEmpty();
        }

        @Override
        public boolean isRun() {
            return handlerThread != null && handlerThread.isAlive();
        }

        @Override
        public void setMaxRunTime(long maxRunTime) {
            this.maxRunTime = maxRunTime;
        }

        @Override
        public void quit() {
            MLog.d(TAG, "TaskWorker quit");

            try {
                handlerThread.quit();
            } catch (Exception e) {
                MLog.e(TAG, e.getMessage());
            }
        }

        @Override
        public boolean isOutOfRunTime() {
            if (startRunTime == 0 || maxRunTime == 0) {
                return false;
            }

            return System.currentTimeMillis() - startRunTime > maxRunTime;
        }
    }
}
