package com.cvshrimp.tool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * Created by CvShrimp on 2019/10/17.
 *
 * @author wkn
 */
public class ThreadPoolManagerUtil {

    private static ThreadPoolManagerUtil threadPoolManagerUtil = new ThreadPoolManagerUtil();

    /**
     * 核心线程数
     */
    private int corePoolSize = 5;
    /**
     * 最大线程数
     */
    private int maximumPoolSize = 60;
    /**
     * 线程池维护线程所允许的空闲时间
     */
    private long keepAliveTime = 2000;
    /**
     * 单例的线程池类
     */
    private ThreadPoolExecutor threadPoolExecutor;
    /**
     * 线程池所使用的缓冲队列的大小
     */
    private int queueSize = 100;

    private boolean initialized = false;

    private ThreadPoolManagerUtil() {
        this.init();
    }

    public static ThreadPoolManagerUtil getInstance() {
        return threadPoolManagerUtil;
    }

    private void init() {
        if (initialized) {
            return;
        }
        this.threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueSize), new ThreadFactoryBuilder().setNameFormat("common-thread").build(), new BlockingQueuePut());
        initialized = true;
    }

    public void addTask(Runnable task) {
        if (!initialized) {
            init();
        }

        threadPoolExecutor.execute(task);
    }


    public <T> Future<T> submitTask(Callable<T> task) {
        if (!initialized) {
            init();
        }
        return threadPoolExecutor.submit(task);
    }


    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public int getActiveCount() {
        return threadPoolExecutor.getActiveCount();
    }

    public void stop() {
        threadPoolExecutor.shutdownNow();
    }

    private static class BlockingQueuePut implements RejectedExecutionHandler {
        /**
         * define the reject policy when executor queue is full
         *
         * @see RejectedExecutionHandler
         * #rejectedExecution(java.lang.Runnable, java.util.concurrent.ThreadPoolExecutor)
         */
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
