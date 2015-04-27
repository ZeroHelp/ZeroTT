package cn.fso.quartz.test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetIpAddress {
	public static void test(){
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
							iplist.add(ip);
						}
					}else{
						//
					}
					
				}
			}
		}catch(SocketException e) {			
			e.printStackTrace();
		}
		System.out.println(iplist.toArray());
		for(InetAddress addr : iplist){
			System.out.println(addr.getHostAddress());
		}
	}
	
	public static void test2(){
		try { 
			InetAddress myIPaddress=InetAddress.getLocalHost();
			System.out.println(myIPaddress.getHostAddress());
		}catch (UnknownHostException e) {
			
		} 
	}
	
	public static boolean matchIpPattern(String str){
		Pattern pattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"); 
		Matcher matcher = pattern.matcher(str); 
		return matcher.matches();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//GetIpAddress.test();
		System.out.println(GetIpAddress.matchIpPattern("192.168.111.111"));
	}

}
