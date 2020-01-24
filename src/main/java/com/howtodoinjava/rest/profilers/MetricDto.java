package com.howtodoinjava.rest.profilers;

import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.collections4.CollectionUtils;

public class MetricDto {
	private List<Long> timeTaken = Collections.emptyList();
	private TreeSet<Long> timeStamps = new TreeSet<>();
	private final int count;
	private final String apiName;
	private final String requestMethod;
	
	public MetricDto(List<Long> timeTaken, List<Long> timeStamps, String apiName, String requestMethod) {
		if(CollectionUtils.isNotEmpty(timeTaken)) {
			this.timeTaken = timeTaken;
			this.timeStamps.addAll(timeStamps);
		}
		this.apiName = apiName;
		this.requestMethod = requestMethod;
		this.count = timeTaken.size();
	}

	public List<Long> getTimeTaken() {
		return timeTaken;
	}

	public int getCount() {
		return count;
	}

	public String getApiName() {
		return apiName;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public TreeSet<Long> getTimeStamps() {
		return timeStamps;
	}
}
