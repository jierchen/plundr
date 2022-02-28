package jier.plundr.service;

import jier.plundr.dto.UserDTO;
import jier.plundr.model.Employee;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    Employee findByEmail(String email);

    List<Employee> findAll(Pageable pageable);

    Employee saveEmployee(Employee employee);

    Employee createEmployee(UserDTO userDto);

    Employee updateEmployee(Long id, UserDTO userDto);

    Boolean deleteEmployee(Long id);
}
