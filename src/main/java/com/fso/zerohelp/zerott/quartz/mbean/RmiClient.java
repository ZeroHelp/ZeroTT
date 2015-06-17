package com.fso.zerohelp.zerott.quartz.mbean;

import java.io.IOException;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RmiClient<T> {
    private static final Logger logger = LoggerFactory.getLogger(RmiClient.class);
	private JMXServiceURL jmxUrl;
	private JMXConnector connector;

	public RmiClient(String targetIp, int port) throws Exception {
		createJMXRmiConnector(targetIp, port);
	}

	public static void main(String[] args) throws Exception {
		RmiClient<ITTControl> rmi = new RmiClient<ITTControl>("localhost", 1099);
		ITTControl ttcontrol = rmi.getTarget(ITTControl.class, "rmi", "name", "ttcontrol");
		System.out.println(ttcontrol.getTTControlInfo(0, 0));
		System.out.println(ttcontrol.getTTControlInfo(1, 0));
		System.out.println(ttcontrol.getTTControlInfo(2, 0));
	}

	private String createJMXConnector(String ip, int port) {
		return "service:jmx:rmi://" + ip + "/jndi/rmi://" + ip + ":" + port
				+ "/ttcontrol";
	}

	private void createJMXRmiConnector(String targetIp, int port) throws Exception {
		jmxUrl = new JMXServiceURL(createJMXConnector(targetIp, port));
		connector = JMXConnectorFactory.connect(jmxUrl);
	}

	public void destroy() {
		if (connector != null) {
			try {
				connector.close();
				connector = null;
			} catch (IOException e) {
			    logger.error(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * 获取目标RMI服务对应的接口对象，实际上是proxy类对象
	 *
	 * @param t 目标RMI服务端口类
	 * @param domain
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public T getTarget(Class<?> t, String domain, String key, String value) throws Exception {
		MBeanServerConnection mBeanServerconnection = connector.getMBeanServerConnection();
		// 获取代理对象
		Object proxy = MBeanServerInvocationHandler.newProxyInstance(
				mBeanServerconnection, new ObjectName(domain, key, value), t, true);
		// 获取测试MBean,并执行它的(暴露出来被管理监控的)方法
		return (T)proxy;
	}

}
