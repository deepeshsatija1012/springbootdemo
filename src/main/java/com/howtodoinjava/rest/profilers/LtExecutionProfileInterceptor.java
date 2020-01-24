package com.howtodoinjava.rest.profilers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Aspect
@Component
@ConditionalOnProperty(value = "profiling.enabled", havingValue = "true", matchIfMissing = false)
public class LtExecutionProfileInterceptor {
	
	private static final class ThreadData {
		private String name;
		private CopyOnWriteArrayList<String> childThreads = new CopyOnWriteArrayList<String>();
		private ConcurrentHashMap<String, CopyOnWriteArrayList<Long>> dataMap = new ConcurrentHashMap<>();
		
		public ThreadData(String name) {
			this.name = name;
		}
		
		public void add(String key, Long value) {
			dataMap.compute(key, (k, v) -> {
				CopyOnWriteArrayList<Long> list = (v==null?new CopyOnWriteArrayList<Long>():v);
				list.add(value);
				return list;
			});
		}
		
		public CopyOnWriteArrayList<String> getChildThreadsSet(){
			return this.childThreads;
		}
		
		public void clear() {
			dataMap.clear();
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("ThreadData [name=").append(name).append(", childThreads=").append(childThreads)
					.append(", dataMap=").append(dataMap).append("]");
			return builder.toString();
		}
	}
	
	private static final ConcurrentHashMap<String, ThreadData> THREAD_LOCAL_MAP = new ConcurrentHashMap<String, ThreadData>();
	private static final ThreadLocal<CopyOnWriteArrayList<String>> MY_THREAD_LOCAL = new ThreadLocal<CopyOnWriteArrayList<String>>() {
		protected CopyOnWriteArrayList<String> initialValue() {
			return THREAD_LOCAL_MAP.get(Thread.currentThread().getName()).getChildThreadsSet();
		};
	};	
	
	@PostConstruct
	public void init() {
		System.out.println();
	}
	
	//execution(* (@RestController *).*(..))
	
	@Pointcut("execution(* (@org.springframework.stereotype.Service *).*(..)) || "
			+ "execution(* (@org.springframework.stereotype.Component *).*(..)) || "
			+ "execution(* (@org.springframework.stereotype.Repository *).*(..)) || "
			+ "execution(* (@org.springframework.stereotype.Controller *).*(..)) || "
			+ "execution(* (@org.springframework.web.bind.annotation.RestController *).*(..))")
	public void spring() {
		
	}

	@Pointcut("execution(* com.howtodoinjava..*(..)) && !within(com.howtodoinjava.rest.profilers.LtInterceptor)")
	public void profilable() {
		System.out.println();
	}
	
	@Pointcut("execution(public * *(..))")
	public void publicMethod() {
		System.out.println();
	}
	
	@Around("spring() && publicMethod() && profilable()")
	public Object annotatedAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();
		try {
			return proceedingJoinPoint.proceed();
		} catch (Throwable t) {
			throw t;
		} finally {
			process(proceedingJoinPoint, System.currentTimeMillis()-startTime);
		}
	}
	
	private void process(JoinPoint joinPoint, long timeTaken) {
		String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
	    String methodName = joinPoint.getSignature().getName();
	    String threadName = MY_THREAD_LOCAL.get().get(MY_THREAD_LOCAL.get().size()-1);
	    
	    THREAD_LOCAL_MAP.get(threadName).add(Thread.currentThread().getName()+"."+className+"."+methodName, timeTaken);
	    
		
	}
	
	public static void initialize() {
		if(Const.IS_PROFILING_ENABLED) {
			THREAD_LOCAL_MAP.compute(Thread.currentThread().getName(), (k, v) -> {
				ThreadData data = (v==null?new ThreadData(Thread.currentThread().getName()):v);
				return data;
			});
			if(MY_THREAD_LOCAL.get().size()==0) {
				MY_THREAD_LOCAL.get().add(Thread.currentThread().getName());
			}
		}
	}
	
	public static CopyOnWriteArrayList<String> getParentThreads(){
		return MY_THREAD_LOCAL.get();
	}
	
	public static void setParent(CopyOnWriteArrayList<String> parents) {
		MY_THREAD_LOCAL.set(parents);
	}
	
	public static Map<String, CopyOnWriteArrayList<Long>> getData(){
		return THREAD_LOCAL_MAP.get(Thread.currentThread().getName()).dataMap;
	}
	
	public static void clear() {
		THREAD_LOCAL_MAP.get(Thread.currentThread().getName()).clear();
	}
}
