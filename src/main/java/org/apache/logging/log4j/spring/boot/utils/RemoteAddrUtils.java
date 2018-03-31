package org.apache.logging.log4j.spring.boot.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * http://blog.csdn.net/caoshuming_500/article/details/20952329
 */
public class RemoteAddrUtils {

	private static String[] headers = new String[]{"Cdn-Src-Ip", "X-Real-IP", "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
	private static String localIP = "127.0.0.1";       
	
	/**
	 * 获取请求客户端IP地址，支持代理服务器
	 * @param request
	 * @return
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		
		// 1、获取客户端IP地址，支持代理服务器
		String remoteAddr = null;
		for (String header : headers) {
			remoteAddr = request.getHeader(header);
			if(StringUtils.isNotEmpty(remoteAddr) && !StringUtils.equals(localIP, "unknown")){
				break;
			}
		}
		// 2、没有取得特定标记的值
		if(StringUtils.isEmpty(remoteAddr) ){
			remoteAddr = request.getRemoteAddr();
		}
		
		// 3、判断是否localhost访问
		if(StringUtils.equals(remoteAddr, "localhost")){
			remoteAddr = localIP;
		}
		 
		return remoteAddr;
	}
}
