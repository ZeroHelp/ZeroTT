package com.fso.zerohelp.zerott.quartz.impl;

import java.lang.reflect.Method;

import org.quartz.JobExecutionContext;

/**
 * 该类实现了StatefulJob，标致着所有的Job都是concurrent=false
 * @author xuming
 *
 */
public class MethodInvokingJob extends ControllableJob{
	private Object targetObject; //目标对象
	private String targetMethod; //目标方法
	private Object[] arguments = new Object[0]; //目标方法的参数
	private String cronExpression; //定时任务表达式设置
	private boolean concurrent; //是否允许并行运行
    private boolean requireJobContext; // 是否需要传入任务上下文对象

	public Object getTargetObject() {
		return targetObject;
	}
	public void setTargetObject(Object targetObject) {
		this.targetObject = targetObject;
	}
	public String getTargetMethod() {
		return targetMethod;
	}
	public void setTargetMethod(String targetMethod) {
		this.targetMethod = targetMethod;
	}
	public Object[] getArguments() {
		return arguments;
	}
	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public boolean isConcurrent() {
		return concurrent;
	}
	public void setConcurrent(boolean concurrent) {
		this.concurrent = concurrent;
	}

    public boolean isRequireJobContext() {
        return requireJobContext;
    }

    public void setRequireJobContext(boolean requireJobContext) {
        this.requireJobContext = requireJobContext;
    }
    
	/**
	 * 按照指定的bean和method执行时间任务，主要是通过反射调用执行
	 * @context 时间任务的环境配置，暂时未使用
	 */
	public void run(JobExecutionContext context){
        Class<?> targetObjectClass = this.targetObject.getClass();
        Object[] arguments = getArguments();
        // 取出设置的参数，如果没有设置，数组长度为空
        Class<?>[] argTypes = new Class[arguments.length];
        for (int i = 0; i < arguments.length; ++i) {
            argTypes[i] = (arguments[i] != null ? arguments[i].getClass() : Object.class);
        }
        try {
            Method method = null;
            if (!isRequireJobContext()) {
                method = targetObjectClass.getMethod(targetMethod, argTypes);
                method.invoke(this.targetObject, getArguments());
            } else {
                Class<?>[] argTypes_ = new Class[arguments.length + 1];
                argTypes_[0] = context.getClass();
                System.arraycopy(argTypes, 0, argTypes_, 1, arguments.length);
                method = targetObjectClass.getMethod(targetMethod, argTypes_);
                Object[] args = new Object[arguments.length + 1];
                args[0] = context;
                System.arraycopy(arguments, 0, args, 1, arguments.length);
                method.invoke(this.targetObject, args);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
	}

}
