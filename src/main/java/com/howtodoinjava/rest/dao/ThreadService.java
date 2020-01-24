package com.howtodoinjava.rest.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.howtodoinjava.rest.model.Employee;
import com.howtodoinjava.rest.profilers.LtExecutors;

@Component
public class ThreadService {
	
	private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(10);
	
	@Autowired
	private EmployeeDAO dao;
	
	public List<Employee> execute() {
		List<Future<Employee>> futures = new ArrayList<>(3);
		futures.add(EXECUTOR.submit(LtExecutors.of(() -> {
			return dao.getEmployeeWithID("0");
		})));
		futures.add(EXECUTOR.submit(LtExecutors.of(() -> {
			return dao.getEmployeeWithID("1");
		})));
		futures.add(EXECUTOR.submit(LtExecutors.of(() -> {
			return dao.getEmployeeWithID("2");
		})));
		
		return futures.stream().map(t -> {
			try {
				return t.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());
	}

}
