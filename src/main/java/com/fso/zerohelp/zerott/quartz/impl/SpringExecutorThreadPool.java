package com.fso.zerohelp.zerott.quartz.impl;

import org.quartz.SchedulerConfigException;
import org.quartz.spi.ThreadPool;
import org.springframework.core.task.TaskExecutor;

public class SpringExecutorThreadPool implements ThreadPool {
    private TaskExecutor taskExecutor;

    public void initialize() throws SchedulerConfigException {
        // Absolutely needs thread-bound TaskExecutor to initialize.
        this.taskExecutor = SchedulerImpl.getTaskExecutor();
        if (this.taskExecutor == null) {
            throw new SchedulerConfigException("No local TaskExecutor found for configuration - 'taskExecutor' property must be set on SchedulerImpl");
        }
    }

    public void shutdown(boolean waitForJobsToComplete) {
    }

    public int getPoolSize() {
        if (taskExecutor instanceof ThreadPool) {
            ThreadPool pool = (ThreadPool) taskExecutor;
            return pool.getPoolSize();
        }
        return -1;
    }

    public boolean runInThread(Runnable runnable) {
        if (runnable == null) {
            return false;
        }
        this.taskExecutor.execute(runnable);
        return true;
    }

    public int blockForAvailableThreads() {
        return 1;
    }

    @Override
    public void setInstanceId(String schedInstId) {

    }

    @Override
    public void setInstanceName(String schedName) {

    }

}
