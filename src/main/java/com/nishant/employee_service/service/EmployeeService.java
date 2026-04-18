package com.nishant.employee_service.service;

import com.nishant.employee_service.dto.EmployeeRequestDto;
import com.nishant.employee_service.dto.EmployeeResponseDto;
import com.nishant.employee_service.entity.Employee;
import com.nishant.employee_service.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeResponseDto createEmployee(EmployeeRequestDto requestDto) {
        Employee employee = new Employee(
                null,
                requestDto.getFirstName(),
                requestDto.getLastName(),
                requestDto.getEmail(),
                requestDto.getDepartment()
        );
        Employee savedEmployee = employeeRepository.save(employee);
        return mapToResponseDto(savedEmployee);
    }

    public List<EmployeeResponseDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public EmployeeResponseDto getEmployeeById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.map(this::mapToResponseDto).orElse(null);
    }

    public EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto requestDto) {
        Optional<Employee> existingEmployee = employeeRepository.findById(id);
        if (existingEmployee.isPresent()) {
            Employee employee = existingEmployee.get();
            employee.setFirstName(requestDto.getFirstName());
            employee.setLastName(requestDto.getLastName());
            employee.setEmail(requestDto.getEmail());
            employee.setDepartment(requestDto.getDepartment());
            Employee updatedEmployee = employeeRepository.save(employee);
            return mapToResponseDto(updatedEmployee);
        }
        return null;
    }

    public boolean deleteEmployee(Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private EmployeeResponseDto mapToResponseDto(Employee employee) {
        return new EmployeeResponseDto(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getDepartment()
        );
    }
}
