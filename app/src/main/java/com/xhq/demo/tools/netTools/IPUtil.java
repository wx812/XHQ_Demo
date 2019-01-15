/** @ProjectName: FHSettings_Mstar
 * @FileName: IPTools.java
 * @PackageName: android.provider.fhagent
 * @Copyright: Copyright (c) 2015
 * @Company: Fiberhome
 * @author: Administrator
 * @date: Dec 18, 2015-2:57:01 PM
 * @version: v1.0
 */
package com.xhq.demo.tools.netTools;

import android.Manifest;
import android.support.annotation.RequiresPermission;
import android.text.TextUtils;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ClassName: IPTools <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * 
 * @author: XHQ
 * @date: Dec 18, 2015-2:57:01 PM
 * @version: v1.0
 * @since: JDK 1.8
 */
public class IPUtil{
	private static final String TAG = IPUtil.class.getName();

	public static boolean ipCheck(String ipString) {
		if(TextUtils.isEmpty(ipString)) return false;
		// split的工作原理是利用正则表达式,而在正则表达式中, "."有特殊意思,所以匹配"."时要用转义字符"\",
		// 所以在正则表达式中匹配"."的表达式是"\.", 而在Java中,\又是特殊字符, 所以还要进行转义, 所以最终变成"\\."
		String[] arrText = ipString.split("\\.");
		final int i = arrText.length;
		final int N = ipString.length();
//		if (N == 0) {
//			return false;
//		} else
		if(ipString.substring(N - 1, N).equals(".") || i != 4){
			return false;
		}else{
			try{
				String V0 = arrText[0];
				String V1 = arrText[1];
				String V2 = arrText[2];
				String V3 = arrText[3];
				int var0 = Integer.parseInt(V0);
				int var1 = Integer.parseInt(V1);
				int var2 = Integer.parseInt(V2);
				int var3 = Integer.parseInt(V3);
				boolean Zero;
				// 运算符优先级 (与 > 或), 单双都是
				Zero = (!V0.substring(0, 1).equals("0") || V0.length() == 1)
						&& (!V1.substring(0, 1).equals("0") || V1.length() == 1)
						&& (!V2.substring(0, 1).equals("0") || V2.length() == 1)
						&& (!V3.substring(0, 1).equals("0") || V3.length() == 1);

				boolean V = (0 <= var0 && var0 <= 255
						&& 0 <= var1 && var1 <= 255
						&& 0 <= var2 && var2 <= 255
						&& 0 <= var3 && var3 <= 255);
				return Zero && V;
			}catch(NumberFormatException e){
				return false;
			}
		}
	}

	public static boolean isIPAddress(String ip) {
		// 测试过, 有效的正则
		String regex = "((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))";
//		String regex = "([0-9]|[0-9]\\d|1\\d{2}|2[0-1]\\d|25[0-5])(\\.(\\d|[0-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(ip);

		// find()方法在部分匹配时和完全匹配时返回true,匹配不上返回false;
		// matches()方法只有在完全匹配时返回true,匹配不上和部分匹配都返回false。
//		return m.find();
		return m.matches();
	}


	public static boolean isValidPort(String port) {
		boolean result = false;
		try {
			int portNum = Integer.valueOf(port);
			if (portNum > 0 && portNum < 65536) {
				result = true;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return result;
	}


	public static boolean checkAddr(String ip, String gateWay, String subnetMask){

		// split的工作原理是利用正则表达式,而在正则表达式中, "."有特殊意思,所以匹配"."时要用转义字符"\",
		// 所以在正则表达式中匹配"."的表达式是"\.", 而在Java中,\又是特殊字符, 所以还要进行转义, 所以最终变成"\\."
		final String reg = "\\.";	// "."
		String[] array_IP = ip.split(reg);
		String[] array_gateway = gateWay.split(reg);
		String[] array_subnetMask = subnetMask.split(reg);

		try{
			if(((Integer.parseInt(array_IP[0]) & Integer.parseInt(array_subnetMask[0]))
					!= (Integer.parseInt(array_gateway[0]) & Integer.parseInt(array_subnetMask[0])))
					|| ((Integer.parseInt(array_IP[1]) & Integer.parseInt(array_subnetMask[1]))
					!= (Integer.parseInt(array_gateway[1]) & Integer.parseInt(array_subnetMask[1])))
					|| ((Integer.parseInt(array_IP[2]) & Integer.parseInt(array_subnetMask[2]))
					!= (Integer.parseInt(array_gateway[2]) & Integer.parseInt(array_subnetMask[2])))
					|| ((Integer.parseInt(array_IP[3]) & Integer.parseInt(array_subnetMask[3]))
					!= (Integer.parseInt(array_gateway[3]) & Integer.parseInt(array_subnetMask[3])))
					|| "0.0.0.0".equals(ip)
					|| "0.0.0.0".equals(gateWay)
					|| "0.0.0.0".equals(subnetMask)){
				return false;
			}
		}catch(Exception e){
			return false;
		}
		return true;
	}


	/**
	 * 判断字符串是否为一个合法的IPv4地址
	 * @param ipAddress ip address
	 * @return {@code true:}是<br>{@code false:}不是
	 */
	public static boolean isValidIPv4Address(String ipAddress) {
		final String regex = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})"
				+ "\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})"
				+ "\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})"
				+ "\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(ipAddress);
		return m.matches();
	}


	@RequiresPermission(Manifest.permission.INTERNET)
	public static boolean isValidIPv6Address(String iPv6Add){
		if(TextUtils.isEmpty(iPv6Add)) return false;
		iPv6Add = iPv6Add.trim();
		if(iPv6Add.substring(0, 1).equals("[") && iPv6Add.substring(iPv6Add.length() - 1).equals("]")){
			iPv6Add = iPv6Add.substring(1, iPv6Add.length() - 1);
		}

		final String regex1 = "^::([\\da-f]{1,4}(:)){0,4}(([\\da-f]{1,4}(:)[\\da-f]{1,4})|([\\da-f]{1,4})" +
				"|(" + "(\\d{1,3}" + ".)" + "{3}\\d{1,3}))";
		final String regex2 = "^([\\da-f]{1,4}(:|::)){1,5}(([\\da-f]{1,4}(:|::)[\\da-f]{1,4})|([\\da-f]{1,4})" +
				"|(" + "(\\d{1,3}.){3}\\d{1,3}))";
		final String regex3 = "^([\\da-f]{1,4}(:|::)){1,7}";
		final String regex4 = "^([\\da-f]{1,4}:){6}(([\\da-f]{1,4} :[\\da-f]{1,4})|((\\d{1,3}.){3}\\d{1,3}))";

		return (1 < Pattern.compile(":").split(iPv6Add).length)
				&& (Pattern.compile(":").split(iPv6Add).length <= 8)
				&& (Pattern.compile("::").split(iPv6Add).length <= 2)
				&& (Pattern.compile("::").split(iPv6Add).length == 2)
				? (((iPv6Add.substring(0, 2).equals("::")) ? Pattern.matches(regex1, iPv6Add)
				: Pattern.matches(regex2, iPv6Add)))
				: ((iPv6Add.substring(iPv6Add.length() - 2).equals("::")) ? Pattern.matches(regex3, iPv6Add)
				: Pattern.matches(regex4, iPv6Add));
	}


	/**
	 * Check if it's "local address" or "link local address" or
	 * "loop back address"
	 *
	 * @param inetAddr inetAddress object
	 * @return result
	 */
	private static boolean isReservedAddr(InetAddress inetAddr) {
		return inetAddr.isAnyLocalAddress() || inetAddr.isLinkLocalAddress() || inetAddr.isLoopbackAddress();
	}


	public static String getIpAddress(boolean isUseIPv4){
		InetAddress inetAddress = getIPAddress(isUseIPv4);
		return inetAddress != null ? inetAddress.getHostAddress() : "";
	}


	/**
	 * 获取设备WiFi网络IPv4地址对应的InetAddress对象
	 *<P>
	 * 根据Android系统的运行机制，当WiFi网络开启时蜂窝网络会自动关闭，因此遍历到的第一个地址是WiFi网卡的IP地址；
	 * 同样，当关闭WiFi网络，打开蜂窝网络时，遍历到的第一个地址是蜂窝网卡的IP地址。
	 *<p>
	 * 当Android手机开启热点的时候，实际上是通过WiFi网卡共享其蜂窝网络，
	 * 因此此时，WiFi网卡和蜂窝网卡分配了不同的IP地址，但由于蜂窝网卡对应的NetworkInterface对象出现的位置要先于WiFi网卡，
	 * 因此该方法返回的实际上是蜂窝网卡的IP地址, 因此加入"wlan0"和"eth0"为常见的WLAN网卡的判断
	 *
	 * @param isUseIPv4 Whether to get IPv4
	 * @return InetAddress
	 */
	@RequiresPermission(Manifest.permission.INTERNET)
	public static InetAddress getIPAddress(boolean isUseIPv4){
		try{
			for(Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
				nis.hasMoreElements(); ){
				NetworkInterface ni = nis.nextElement();
				if(!ni.isUp()) continue;   // 防止小米手机返回10.0.2.15
				// "wlan0"和"eth0"为常见的WLAN网卡的DisplayName名称
				if(ni.getDisplayName().equals("wlan0") || ni.getDisplayName().equals("eth0")){
					for(Enumeration<InetAddress> addr = ni.getInetAddresses(); addr.hasMoreElements(); ){
						InetAddress inetAddress = addr.nextElement();
						if(!inetAddress.isLoopbackAddress()){
//                        String hostAddress = inetAddress.getHostAddress();
//                        boolean isIPv4 = hostAddress.indexOf(':') < 0;
							if(isUseIPv4 && inetAddress instanceof Inet4Address){
								return inetAddress;
							}else{
								if(inetAddress instanceof Inet6Address){
//                                int index = hostAddress.indexOf('%');
//                                String s1 = hostAddress.toUpperCase();
//                                String s2 = hostAddress.substring(0, index).toUpperCase();
//                                return index < 0 ?  s1: s2;
									return inetAddress;
								}
							}
						}
					}
				}
			}
		}catch(SocketException e){
			e.printStackTrace();
		}
		return null;
	}

	@RequiresPermission(Manifest.permission.INTERNET)
	public static String getIPv6Address(){
		InetAddress inetAddress = getIPAddress(false);
		String ipAddr = null;
		if(inetAddress != null){
			if(inetAddress.isSiteLocalAddress() && !isReservedAddr(inetAddress)){
				ipAddr = inetAddress.getHostAddress();
				int index = ipAddr.indexOf('%');
				if (index > 0) {
					ipAddr = ipAddr.substring(0, index);
				}
			}
		}
		return ipAddr;
	}


	/**
	 * get ip address based on domain name
	 *
	 * @param domain domain name
	 * @return ip address
	 */
	@RequiresPermission(Manifest.permission.INTERNET)
	public static String getIPAddressByDomain(final String domain){
		try{
			ExecutorService exec = Executors.newCachedThreadPool();
			Future<String> fs = exec.submit(new Callable<String>(){
				@Override
				public String call(){
					try{
						InetAddress inetAddress = InetAddress.getByName(domain);
						return inetAddress.getHostAddress();
					}catch(UnknownHostException e){
						e.printStackTrace();
					}
					return null;
				}
			});
			return fs.get();
		}catch(InterruptedException | ExecutionException e){
			e.printStackTrace();
		}
		return null;
	}
}
