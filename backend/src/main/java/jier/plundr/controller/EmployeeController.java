package jier.plundr.controller;

import jier.plundr.dto.employee.CreateEmployeeDTO;
import jier.plundr.dto.employee.UpdateEmployeeDTO;
import jier.plundr.model.Employee;
import jier.plundr.service.EmployeeService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // ------------------------ Admin Requests -------------------------//

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getEmployees(@RequestParam int page, @RequestParam int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            List<Employee> employees = employeeService.findAll(pageable);

            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable("id") long employeeId) {
        try {
            Optional<Employee> optionalEmployee = employeeService.findById(employeeId);

            if(optionalEmployee.isPresent()) {
                return new ResponseEntity<>(optionalEmployee.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/employee")
    public ResponseEntity<Employee> createEmployee(@RequestBody CreateEmployeeDTO createEmployeeDto) {
        try {
            Employee newEmployee = employeeService.createEmployee(createEmployeeDto);

            return new ResponseEntity<>(newEmployee, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/employee/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") long employeeId,
                                                   @RequestBody UpdateEmployeeDTO updateEmployeeDto) {
        try {
            Employee employee = employeeService.updateEmployee(employeeId, updateEmployeeDto);

            return new ResponseEntity<>(employee, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") long employeeId) {
        try {
            Boolean isDeleted = employeeService.deleteEmployee(employeeId);

            if(isDeleted) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
