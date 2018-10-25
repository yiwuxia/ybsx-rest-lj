package com.ybsx.base.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 请求拦截器，处理跨域问题
 * @author zhouKai
 * 2018年4月17日 上午10:43:02
 */
public class CorsInterceptor implements HandlerInterceptor {

	private List<String> excludedUrls;

	public List<String> getExcludedUrls() {
		return excludedUrls;
	}

	public void setExcludedUrls(List<String> excludedUrls) {
		this.excludedUrls = excludedUrls;
	}

	/**
	 * 
	 * 在业务处理器处理请求之前被调用 
	 * 	如果返回false 
	 * 		从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链, 
	 * 	如果返回true 
	 * 		执行下一个拦截器,直到所有的拦截器都执行完毕 再执行被拦截的Controller
	 * 然后进入拦截器链,
	 * 从最后一个拦截器往回执行所有的postHandle()
	 * 接着再从最后一个拦截器往回执行所有的afterCompletion()
	 * 
	 * @param  request
	 * @param  response
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String header = request.getHeader("Access-Control-Request-Headers");
		String token = request.getHeader("token");
//		System.out.println("CorsInterceptor.preHandle()" + request.getRequestURI() + ", Access-Control-Request-Headers=" + header);
//		System.out.println("request.getHeader(\"token\")=" + token);
//		System.out.println("request.getParameter(\"token\")=" + request.getParameter("token"));
//		System.out.println("request.getHeader(\"Authorization\")=" + request.getHeader("Authorization"));
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "*");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, token, Authorization, *");
//		response.setHeader("Access-Control-Allow-Headers", "*");
		return true;
	}

	// 在业务处理器处理请求执行完成后,生成视图之前执行的动作
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	/**
	 * 
	 * 在DispatcherServlet完全处理完请求后被调用
	 * 当有拦截器抛出异常时,
	 * 会从当前拦截器往回执行所有的拦截器的afterCompletion()
	 * @param request
	 * @param response
	 * @param handler
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}
}
