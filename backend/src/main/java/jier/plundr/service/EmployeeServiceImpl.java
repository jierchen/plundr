package jier.plundr.service;

import jier.plundr.dto.ReturnPageDTO;
import jier.plundr.dto.employee.CreateEmployeeDTO;
import jier.plundr.dto.employee.UpdateEmployeeDTO;
import jier.plundr.mapper.PageMapper;
import jier.plundr.model.Employee;
import jier.plundr.model.enums.UserType;
import jier.plundr.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private final EmployeeRepository employeeRepository;

    @Autowired
    private final PageMapper pageMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, PageMapper pageMapper) {
        this.employeeRepository = employeeRepository;
        this.pageMapper = pageMapper;
    }

    /**
     * Finds all {@code Employees}.
     *
     * @param pageable {@code Pageable} object containing pagination information to limit search results.
     * @return Page information of found {@code Employees}.
     */
    @Override
    public ReturnPageDTO<Employee> findAll(Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findAll(pageable);
        return pageMapper.pageToReturnPageDTO(employeePage);
    }

    /**
     * Finds an {@code Employee} by {@code id}.
     *
     * @param employeeId {@code id} of {@code Employee} to find.
     * @return An {@code Optional} object containing the found {@code Employee}
     *         or {@code null} if {@code Employee} is not found.
     */
    @Override
    public Optional<Employee> findById(Long employeeId) {
        return employeeRepository.findById(employeeId);
    }

    /**
     * Saves a {@code Employee} to keep changes made.
     *
     * @param employee {@code Employee} to save.
     * @return {@code Employee} saved.
     */
    @Override
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    /**
     * Creates and saves a new {@code Employee}
     *
     * @param createEmployeeDto {@code CreateEmployeeDTO} containing information for {@code Employee} creation
     * @return {@code Employee} created and saved
     */
    @Override
    public Employee createEmployee(CreateEmployeeDTO createEmployeeDto) {
        Employee newEmployee = new Employee();

        // User fields
        newEmployee.setFirstName(createEmployeeDto.getFirstName());
        newEmployee.setLastName(createEmployeeDto.getLastName());
        newEmployee.setPhoneNumber(createEmployeeDto.getPhoneNumber());
        newEmployee.setDateOfBirth(createEmployeeDto.getDateOfBirth());
        newEmployee.setEmail(createEmployeeDto.getEmail());
        newEmployee.setUsername(createEmployeeDto.getUsername());
        newEmployee.setPassword(createEmployeeDto.getPassword());
        newEmployee.setType(UserType.EMPLOYEE);

        // Employee fields
        newEmployee.setSalary(createEmployeeDto.getSalary());

        return this.saveEmployee(newEmployee);
    }

    /**
     * Updates and saves an existing {@code Employee}.
     *
     * @param employeeId @{code id} of {@code Employee} to update.
     * @param updateEmployeeDto  {@code UpdateEmployeeDTO} containing information for {@code Employee} updating.
     * @return {@code Employee} updated and saved.
     */
    @Override
    public Employee updateEmployee(Long employeeId, UpdateEmployeeDTO updateEmployeeDto) {
        Employee employee = employeeRepository.getById(employeeId);

        // User fields
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

        // Employee fields
        if(updateEmployeeDto.getSalary() != null)
            employee.setSalary(updateEmployeeDto.getSalary());

        return this.saveEmployee(employee);
    }

    /**
     * Deletes an existing {@code Employee}.
     *
     * @param employeeId {@code id} of {@code Employee} to delete.
     * @return Return {@code True} if found {@code Employee} has been successfully deleted, {@code False} otherwise.
     */
    @Override
    public Boolean deleteEmployee(Long employeeId) {
        if(employeeRepository.existsById(employeeId)) {
            employeeRepository.deleteById(employeeId);
            return true;
        }
        return false;
    }
}
