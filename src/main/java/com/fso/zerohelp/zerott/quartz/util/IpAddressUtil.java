package com.fso.zerohelp.zerott.quartz.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IpAddressUtil {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(IpAddressUtil.class);
    
	/**
	 * 获取IP地址列表
	 * @return IP地址列表
	 */
	public static List<InetAddress> getIpAddressList(){
		List<InetAddress> iplist = new ArrayList<InetAddress>();
		Enumeration<NetworkInterface> netInterfaces;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			while(netInterfaces.hasMoreElements())  
			{  
				NetworkInterface ni = netInterfaces.nextElement(); 
				Enumeration<InetAddress> ips = ni.getInetAddresses();
				while(ips.hasMoreElements()){
					InetAddress ip= ips.nextElement();
					if(ip instanceof Inet4Address){
						//IPv4才处理
						if(!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":")==-1){
							if(!ip.equals("127.0.0.1")){
								iplist.add(ip);
							}
							
						}
					}else{
						//
					}
					
				}
			}
		}catch(SocketException e) {
		    LOGGER.error(e.getMessage(), e);
		}
		return iplist;
	}
	
	public static boolean matchIP(String targetIp){
		List<InetAddress> ipAddr = IpAddressUtil.getIpAddressList();
		for(InetAddress ip : ipAddr){
			System.out.println(ip.getHostAddress());
			if(ip.getHostAddress().equals(targetIp)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean matchIpPattern(String str){
		if(str == null) return false;
		Pattern pattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"); 
		Matcher matcher = pattern.matcher(str); 
		return matcher.matches();
	}
}
