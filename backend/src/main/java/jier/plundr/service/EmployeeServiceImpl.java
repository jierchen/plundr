package jier.plundr.service;

import jier.plundr.dto.EmployeeDTO;
import jier.plundr.dto.UserDTO;
import jier.plundr.model.Employee;
import jier.plundr.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> findAll(Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findAll(pageable);
        return employeePage.getContent();
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Employee createEmployee(UserDTO userDto, EmployeeDTO employeeDTO) {
        Employee newEmployee = new Employee();

        newEmployee.setFirstName(userDto.getFirstName());
        newEmployee.setLastName(userDto.getLastName());
        newEmployee.setPhoneNumber(userDto.getPhoneNumber());
        newEmployee.setDateOfBirth(userDto.getDateOfBirth());
        newEmployee.setEmail(userDto.getEmail());
        newEmployee.setUsername(userDto.getUsername());
        newEmployee.setPassword(userDto.getPassword());

        newEmployee.setSalary(employeeDTO.getSalary());

        return this.saveEmployee(newEmployee);
    }

    @Override
    public Employee updateEmployee(Long employeeId, UserDTO userDto, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.getById(employeeId);

        employee.setFirstName(userDto.getFirstName());
        employee.setLastName(userDto.getLastName());
        employee.setPhoneNumber(userDto.getPhoneNumber());
        employee.setDateOfBirth(userDto.getDateOfBirth());
        employee.setEmail(userDto.getEmail());
        employee.setUsername(userDto.getUsername());
        employee.setPassword(userDto.getPassword());

        employee.setSalary(employeeDTO.getSalary());

        return this.saveEmployee(employee);
    }

    @Override
    public Boolean deleteEmployee(Long employeeId) {
        if(employeeRepository.existsById(employeeId)) {
            employeeRepository.deleteById(employeeId);
            return true;
        }
        return false;
    }
}
