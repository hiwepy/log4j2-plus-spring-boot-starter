package org.apache.logging.log4j.spring.boot.ext.web;

import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.spring.boot.utils.RemoteAddrUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 用于添加请求参数到 {@link ThreadContext} 中，可使用 %X{}获取指定的值，如 %X{uuid}
 * http://logging.apache.org/log4j/2.x/manual/thread-context.html
 */
public class Log4j2MDCInterceptor implements HandlerInterceptor {

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)

			throws Exception {

		ThreadContext.put("uuid", UUID.randomUUID().toString()); // Add the fishtag;
		ThreadContext.put("requestURL", request.getRequestURL().toString());
		ThreadContext.put("requestURI", request.getRequestURI());
		ThreadContext.put("queryString", request.getQueryString());
		ThreadContext.put("remoteAddr", RemoteAddrUtils.getRemoteAddr(request));
		ThreadContext.put("remoteHost", request.getRemoteHost());
		ThreadContext.put("remotePort", String.valueOf(request.getRemotePort()));
		ThreadContext.put("localAddr", request.getLocalAddr());
		ThreadContext.put("localName", request.getLocalName());

		Enumeration<?> enu = request.getSession().getAttributeNames();
		while (enu.hasMoreElements()) {
			String key = (String) enu.nextElement();
			Object val = request.getSession().getAttribute(key);
			if (val instanceof String) {
				ThreadContext.put("session." + key, String.valueOf(val));
			}
		}

		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception exception) throws Exception {
		
		ThreadContext.clearMap();
	}

}
