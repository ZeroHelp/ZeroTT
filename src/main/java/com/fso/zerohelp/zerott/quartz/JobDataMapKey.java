/**
 * 
 */
package com.fso.zerohelp.zerott.quartz;

/**
 * @author yinqiang
 * 
 */
public final class JobDataMapKey {

    /**
     * 任务对象
     */
    public static final String RUN_OBJECT = "runObject";

    /**
     * 任务scheduler对象
     */
    public static final String RUN_SCHEDULER = "runScheduler";

    /**
     * 定时任务的名称，英文简称
     */
    public static final String RUN_TASK_ID = "runTaskId";

    /**
     * 定时任务的数字ID
     */
    public static final String RUN_ID = "runId";

    /**
     * 任务trigger对象
     */
    public static final String RUN_TRIGGER = "runTrigger";

    /**
     * 任务magic参数，具体任务负责解析
     */
    public static final String RUN_MAGIC = "runMagic";

}
