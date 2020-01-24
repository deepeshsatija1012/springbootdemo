package com.howtodoinjava.rest.profilers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.common.collect.Lists;

public class MyMetric {
	private final String name;
	private final String requestMethod;
	private CopyOnWriteArrayList<Long> times = new CopyOnWriteArrayList<>();
	private CopyOnWriteArrayList<Long> startTimes = new CopyOnWriteArrayList<>();
	public MyMetric(String name, String requestMethod) {
		this.name = name;
		this.requestMethod = requestMethod;
	}
	public void update(long time, long startTime) {
		times.add(time);
		startTimes.add(startTime);
	}
	
	public void reset() {
		this.times = new CopyOnWriteArrayList<Long>();
		this.startTimes = new CopyOnWriteArrayList<Long>();
	}
	
	public List<List<Long>> getAndClearTimes(){
		List<Long> timesTaken = new ArrayList<>(this.times);
		List<Long> timesStamps = new ArrayList<>(this.startTimes);
		reset();
		return Lists.newArrayList(timesTaken, timesStamps);
	}
	public String getRequestMethod() {
		return requestMethod;
	}
	
	public String getName() {
		return this.name;
	}
	
	

}
