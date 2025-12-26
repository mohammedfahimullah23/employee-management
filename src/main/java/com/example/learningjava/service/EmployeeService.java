package com.example.learningjava.service;

import com.example.learningjava.azure_functions.AzureFunctionClient;
import com.example.learningjava.model.Employee;
import com.example.learningjava.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;
    private final AzureFunctionClient azureFunctionClient;

    public EmployeeService(EmployeeRepository repository, AzureFunctionClient azureFunctionClient) {
        this.repository = repository;
        this.azureFunctionClient = azureFunctionClient;
    }

    public Employee create(Employee emp) {
        return repository.save(emp);
    }

    public Employee createFromAzureFunctions(Employee employee){
        return this.azureFunctionClient.processEmployee(employee);
    }

    public List<Employee> getAll() {
        return repository.findAll();
    }

    public Employee getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public Employee update(Long id, Employee emp) {
        Employee existing = getById(id);
        existing.setName(emp.getName());
        existing.setEmail(emp.getEmail());
        existing.setDepartment(emp.getDepartment());
        existing.setSalary(emp.getSalary());
        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}

