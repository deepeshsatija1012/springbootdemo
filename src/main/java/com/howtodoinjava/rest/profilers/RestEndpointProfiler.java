package com.howtodoinjava.rest.profilers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.actuate.endpoint.EndpointProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@ConfigurationProperties(prefix = "endpoints.apimetrics")
public class RestEndpointProfiler implements Endpoint<List<MetricDto>>, EnvironmentAware{
	private Environment environment;
	private ConcurrentMap<String, MyMetric> metricCache = new ConcurrentHashMap<>();
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
	private boolean enabled;
	
	@PostConstruct
	public void init() {
		executor.schedule(() -> {
		    metricCache.entrySet().stream().forEach(e -> e.getValue().reset());
		}, 10, TimeUnit.MINUTES);
	}
	
	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void loggable() {
	}
	
	@Around("loggable()")
	public Object annotatedAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();
		try {
			return proceedingJoinPoint.proceed();
		}catch(Throwable t) {
			throw t;
		}finally {
			long endTime = System.currentTimeMillis();
			long timeDiff = endTime - startTime;
			HttpServletRequest request =
					((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			RequestMethod method = RequestMethod.valueOf(request.getMethod());
			if(method != null) {
			    String apiName = request.getMethod()+"."+getStringKey(proceedingJoinPoint, request, method);
				metricCache.compute(apiName, (key, value) -> {
					MyMetric metric = value;
					if(metric==null) {
						metric = getMetricObject(proceedingJoinPoint, request, method);
					}
					metric.update(timeDiff, startTime);
					return metric;
				});
			}
		}
	}
	
	public String getStringKey(ProceedingJoinPoint proceedingJoinPoint, HttpServletRequest request, RequestMethod requestMethod) {
		MethodSignature methodSignature =((MethodSignature) proceedingJoinPoint.getSignature());
		
		Class<?> clazz = methodSignature.getDeclaringType();
		Method method = methodSignature.getMethod();
		RequestMapping classLevelRequestMapping = clazz.getAnnotation(RequestMapping.class);
		StringBuilder builder = new StringBuilder(classLevelRequestMapping==null || ArrayUtils.isEmpty(classLevelRequestMapping.path())?StringUtils.EMPTY:classLevelRequestMapping.path()[0]);
		switch (requestMethod) {
			case GET:
				GetMapping getMapping = clazz.getAnnotation(GetMapping.class);
				builder.append(getMapping==null || ArrayUtils.isEmpty(getMapping.path())?StringUtils.EMPTY:getMapping.path()[0]);
				break;
			case HEAD:
				break;
			case POST:
				PostMapping postMapping = clazz.getAnnotation(PostMapping.class);
				builder.append(postMapping==null || ArrayUtils.isEmpty(postMapping.path())?StringUtils.EMPTY:postMapping.path()[0]);
				break;
			case PUT:
				PutMapping putMapping = clazz.getAnnotation(PutMapping.class);
				builder.append(putMapping==null || ArrayUtils.isEmpty(putMapping.path())?StringUtils.EMPTY:putMapping.path()[0]);
				break;
			case PATCH:
				break;
			case DELETE:
				DeleteMapping deleteMapping = clazz.getAnnotation(DeleteMapping.class);
				builder.append(deleteMapping==null || ArrayUtils.isEmpty(deleteMapping.path())?StringUtils.EMPTY:deleteMapping.path()[0]);
				break;
			case OPTIONS:
				break;
			case TRACE:
				break;
			default:
				break;
		}
		
		RequestMapping methodLevelRequestMapping = method.getAnnotation(RequestMapping.class);
		builder.append(methodLevelRequestMapping==null || ArrayUtils.isEmpty(methodLevelRequestMapping.path())?StringUtils.EMPTY:methodLevelRequestMapping.path()[0]);
		switch (requestMethod) {
			case GET:
				GetMapping getMapping = method.getAnnotation(GetMapping.class);
				builder.append(getMapping==null || ArrayUtils.isEmpty(getMapping.path())?StringUtils.EMPTY:getMapping.path()[0]);
				break;
			case HEAD:
				break;
			case POST:
				PostMapping postMapping = method.getAnnotation(PostMapping.class);
				builder.append(postMapping==null || ArrayUtils.isEmpty(postMapping.path())?StringUtils.EMPTY:postMapping.path()[0]);
				break;
			case PUT:
				PutMapping putMapping = method.getAnnotation(PutMapping.class);
				builder.append(putMapping==null || ArrayUtils.isEmpty(putMapping.path())?StringUtils.EMPTY:putMapping.path()[0]);
				break;
			case PATCH:
				break;
			case DELETE:
				DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
				builder.append(deleteMapping==null || ArrayUtils.isEmpty(deleteMapping.path())?StringUtils.EMPTY:deleteMapping.path()[0]);
				break;
			case OPTIONS:
				break;
			case TRACE:
				break;
			default:
				break;
		}
		
		return builder.toString();
	}
	
	public MyMetric getMetricObject(ProceedingJoinPoint proceedingJoinPoint, HttpServletRequest request, RequestMethod requestMethod) {
		return new MyMetric(getStringKey(proceedingJoinPoint, request, requestMethod), request.getMethod());
	}

	@Override
	public String getId() {
		return "apimetrics";
	}

	@Override
	public boolean isEnabled() {
		return EndpointProperties.isEnabled(this.environment, this.enabled);
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean isSensitive() {
		return false;
	}

	@Override
	public List<MetricDto> invoke() {
		List<MetricDto> list = new ArrayList<>();
		for(Map.Entry<String, MyMetric> entry : this.metricCache.entrySet()) {
			List<List<Long>> times = entry.getValue().getAndClearTimes();
			list.add(new MetricDto(times.get(0), times.get(1), entry.getValue().getName(), entry.getValue().getRequestMethod()));
		}
		return list;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

}
