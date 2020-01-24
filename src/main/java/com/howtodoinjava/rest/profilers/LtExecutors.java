package com.howtodoinjava.rest.profilers;

import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

public class LtExecutors {
	
	public static Runnable of(Runnable r) {
		if(Const.IS_PROFILING_ENABLED)
			return new MyRunnable(r);
		else
			return r;
	}
	
	public static <V> Callable<V> of(Callable<V> c) {
		if(Const.IS_PROFILING_ENABLED)
			return new MyCallable<V>(c);
		else
			return c;
	}
	
	private static class MyRunnable implements Runnable {
		
		private Runnable delegate;
		private CopyOnWriteArrayList<String> parentThreadName;
		
		
		private MyRunnable(Runnable delegate) {
			this.delegate = delegate;
			this.parentThreadName = LtExecutionProfileInterceptor.getParentThreads();
		}

		@Override
		public void run() {
			LtExecutionProfileInterceptor.setParent(parentThreadName);
			delegate.run();
		}
		
	}
	
	private static class MyCallable<V> implements Callable<V> {
		
		private Callable<V> delegate;
		private CopyOnWriteArrayList<String> parentThreadName;
		
		
		private MyCallable(Callable<V> delegate) {
			this.delegate = delegate;
			this.parentThreadName = LtExecutionProfileInterceptor.getParentThreads();
		}


		@Override
		public V call() throws Exception {
			LtExecutionProfileInterceptor.setParent(parentThreadName);
			return delegate.call();
		}
		
	}
	
    /** Cannot instantiate. */
    private LtExecutors() {}
	
}
