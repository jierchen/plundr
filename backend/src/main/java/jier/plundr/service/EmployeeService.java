package jier.plundr.service;

import jier.plundr.dto.EmployeeDTO;
import jier.plundr.dto.UserDTO;
import jier.plundr.model.Employee;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    List<Employee> findAll(Pageable pageable);

    Employee saveEmployee(Employee employee);

    Employee createEmployee(UserDTO userDto, EmployeeDTO employeeDTO);

    Employee updateEmployee(Long employeeId, UserDTO userDto, EmployeeDTO employeeDTO);

    Boolean deleteEmployee(Long employeeId);
}
