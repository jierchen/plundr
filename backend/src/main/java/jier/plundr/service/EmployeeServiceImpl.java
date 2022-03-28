package jier.plundr.service;

import jier.plundr.dto.employee.CreateEmployeeDTO;
import jier.plundr.dto.customer.CreateCustomerDTO;
import jier.plundr.dto.employee.UpdateEmployeeDTO;
import jier.plundr.model.Employee;
import jier.plundr.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public Optional<Employee> findById(Long employeeId) {
        return employeeRepository.findById(employeeId);
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Employee createEmployee(CreateEmployeeDTO createEmployeeDto) {
        Employee newEmployee = new Employee();

        newEmployee.setFirstName(createEmployeeDto.getFirstName());
        newEmployee.setLastName(createEmployeeDto.getLastName());
        newEmployee.setPhoneNumber(createEmployeeDto.getPhoneNumber());
        newEmployee.setDateOfBirth(createEmployeeDto.getDateOfBirth());
        newEmployee.setEmail(createEmployeeDto.getEmail());
        newEmployee.setUsername(createEmployeeDto.getUsername());
        newEmployee.setPassword(createEmployeeDto.getPassword());

        newEmployee.setSalary(createEmployeeDto.getSalary());

        return this.saveEmployee(newEmployee);
    }

    @Override
    public Employee updateEmployee(Long employeeId, UpdateEmployeeDTO updateEmployeeDto) {
        Employee employee = employeeRepository.getById(employeeId);

        if(updateEmployeeDto.getFirstName() != null)
            employee.setFirstName(updateEmployeeDto.getFirstName());
        if(updateEmployeeDto.getLastName() != null)
            employee.setLastName(updateEmployeeDto.getLastName());
        if(updateEmployeeDto.getPhoneNumber() != null)
            employee.setPhoneNumber(updateEmployeeDto.getPhoneNumber());
        if(updateEmployeeDto.getDateOfBirth() != null)
            employee.setDateOfBirth(updateEmployeeDto.getDateOfBirth());
        if(updateEmployeeDto.getEmail() != null)
            employee.setEmail(updateEmployeeDto.getEmail());
        if(updateEmployeeDto.getUsername() != null)
            employee.setUsername(updateEmployeeDto.getUsername());
        if(updateEmployeeDto.getPassword() != null)
            employee.setPassword(updateEmployeeDto.getPassword());

        if(updateEmployeeDto.getSalary() != null)
            employee.setSalary(updateEmployeeDto.getSalary());

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
