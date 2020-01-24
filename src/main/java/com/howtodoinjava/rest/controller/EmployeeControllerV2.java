package com.howtodoinjava.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.howtodoinjava.rest.model.Employees;

@RestController
@RequestMapping(path = "/employeesv2")
public class EmployeeControllerV2 {

	@Autowired
	private EmployeeController employeeDao;
	@GetMapping(path = "/", produces = "application/json")
	public Employees getEmployees() {
		return employeeDao.getEmployees();
	}

	@PostMapping(path = "/", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Void> addEmployee() {
		return employeeDao.addEmployee();
	}
}
