package com.howtodoinjava.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.howtodoinjava.rest.dao.ThreadService;
import com.howtodoinjava.rest.model.Employee;

@RestController
@RequestMapping(path = "/threads")
public class ThreadController {
	
	@Autowired
	private ThreadService threadService;
	
	@GetMapping(path = "/", produces = "application/json")
	public List<Employee> getEmployees() {
		return threadService.execute();
	}

}
