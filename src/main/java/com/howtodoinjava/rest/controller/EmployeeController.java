package com.howtodoinjava.rest.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.howtodoinjava.rest.dao.EmployeeDAO;
import com.howtodoinjava.rest.model.Employee;
import com.howtodoinjava.rest.model.Employees;

@RestController
@RequestMapping(path = "/employees")
public class EmployeeController {
	@Autowired
	private EmployeeDAO employeeDao;
	
	@GetMapping(path = "/", produces = "application/json")
	public Employees getEmployees() {
		return employeeDao.getAllEmployees();
	}
	
	@GetMapping(path = "/{id}", produces = "application/json")
	public Employee getEmployeesWithId(@PathVariable(value = "id") String id) {
		return employeeDao.getEmployeeWithID(id);
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/1", produces = "application/json")
	public Employees getEmployees1() {
		return employeeDao.getAllEmployees();
	}

	@RequestMapping(path = "/add/", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Void> addEmployee() {
		Integer ids = employeeDao.getAllEmployees().getEmployeeList().size() + 1;
		Employee ee = new Employee(ids, "ABC", "DEF", "abc@def.com");

		employeeDao.addEmployee(ee);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(ee.getId()-1)
				.toUri();
		return ResponseEntity.created(location).build();
	}
}