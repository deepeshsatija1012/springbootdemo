package com.howtodoinjava.rest.profilers;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component("lt")
public class LtInterceptor implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(Const.IS_PROFILING_ENABLED) {
			LtExecutionProfileInterceptor.initialize();
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		if(Const.IS_PROFILING_ENABLED) {
			Map<String, CopyOnWriteArrayList<Long>> executionTimeMap = LtExecutionProfileInterceptor.getData();
			System.out.println("Execution Time : " + executionTimeMap);
			LtExecutionProfileInterceptor.clear();
		}
	}

}
