package com.example.Personnel.controller;
import com.example.Personnel.model.Employee;
import com.example.Personnel.service.EmployeeService;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Long id) {
        System.out.println("Fetching employee with ID: " + id);
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        if (employee.isPresent()) {
            return ResponseEntity.ok(employee.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Employee addEmployee(@RequestBody Employee employee) {
        return employeeService.addEmployee(employee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable("id") Long id,
            @RequestBody Employee employee) {
        Optional<Employee> existingEmployee = employeeService.getEmployeeById(id);
        if (existingEmployee.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        employee.setId(id);
        Employee updatedEmployee = employeeService.addEmployee(employee);

        return ResponseEntity.ok(updatedEmployee);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Employee> patchEmployee(
            @PathVariable("id") Long id,
            @RequestBody Map<String, Object> changes) {
        Optional<Employee> existingEmployee = employeeService.getEmployeeById(id);
        if (existingEmployee.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (changes.containsKey("name")) {
            existingEmployee.get().setName(changes.get("name").toString());
        }
        if (changes.containsKey("position")) {
            existingEmployee.get().setPosition(changes.get("position").toString());
        }
        if (changes.containsKey("salary")) {
            existingEmployee.get().setSalary(Double.parseDouble(changes.get("salary").toString()));
        }

        Employee updatedEmployee = employeeService.addEmployee(existingEmployee.get());

        return ResponseEntity.ok(updatedEmployee);
    }
    @RequestMapping("/{id}")
    public void findEmployee(@PathVariable("id") long id) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        if (employee.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        }
    }
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptions() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Allow", "GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD");
        return ResponseEntity.ok().headers(headers).build();
    }
}


