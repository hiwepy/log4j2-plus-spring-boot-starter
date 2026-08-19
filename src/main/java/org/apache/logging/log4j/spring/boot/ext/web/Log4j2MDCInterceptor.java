package org.apache.logging.log4j.spring.boot.ext.web;

import java.util.Enumeration;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.spring.boot.utils.RemoteAddrUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 用于添加请求参数到 {@link ThreadContext} 中，可使用 %X{}获取指定的值，如 %X{uuid}
 * http://logging.apache.org/log4j/2.x/manual/thread-context.html
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class Log4j2MDCInterceptor implements HandlerInterceptor {
	/**
	 * <p>Pre handle.</p>
	 * @param request the request
	 * @param response the response
	 * @param handler the handler
	 * @return the boolean
	 */

	@Override
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
		
		Enumeration<String> names = request.getHeaderNames();
		while (names.hasMoreElements()) {
			String key = names.nextElement();
			ThreadContext.put("header." + key, request.getHeader(key));
		}
		
		Enumeration<String> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String key = params.nextElement();
			ThreadContext.put("param." + key, request.getParameter(key));
		}
		
		/*HttpSession session = request.getSession(false);
		if(session != null) {
			Enumeration<?> enu = session.getAttributeNames();
			while (enu.hasMoreElements()) {
				String key = (String) enu.nextElement();
				Object val = session.getAttribute(key);
				if (val instanceof String) {
					ThreadContext.put("session." + key, String.valueOf(val));
				}
			}
		}*/

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception exception) throws Exception {
		
		ThreadContext.clearMap();
	}

}
