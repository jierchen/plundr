package jier.plundr.service;

import jier.plundr.dto.ReturnPageDTO;
import jier.plundr.dto.employee.CreateEmployeeDTO;
import jier.plundr.dto.employee.UpdateEmployeeDTO;
import jier.plundr.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    ReturnPageDTO<Employee> findAll(Pageable pageable);

    Optional<Employee> findById(Long employeeId);

    Employee saveEmployee(Employee employee);

    Employee createEmployee(CreateEmployeeDTO createEmployeeDto);

    Employee updateEmployee(Long employeeId, UpdateEmployeeDTO updateEmployeeDto);

    Boolean deleteEmployee(Long employeeId);
}
