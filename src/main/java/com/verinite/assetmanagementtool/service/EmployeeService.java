package com.verinite.assetmanagementtool.service;

import com.verinite.assetmanagementtool.dto.EmployeeDto;
import com.verinite.assetmanagementtool.dto.EmployeeExportDto;
import com.verinite.assetmanagementtool.entity.EmployeeEntity;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface EmployeeService {

    public EmployeeDto saveEmployee(EmployeeDto employeeDTO);

    List<EmployeeEntity> allEmployees();

    ResponseEntity<?> getById(String empId);

    List<EmployeeEntity> getActiveAccounts(String str);

    ResponseEntity<?> getAllByLocation(String str);

    Object updateEmp(String empId, EmployeeDto employee);

    ResponseEntity<?> deleteEmployeeById(String empId);

    ResponseEntity<?> importEmployeeFromExcel(InputStream inputStream) throws IOException;

    void exportEmployeesToExcel(List<EmployeeExportDto> data, OutputStream outputStream) throws IOException;

    List<String> getUniqueEmployeeLocation();

    List<String> getUniqueEmployeeDesignation();

    List<String> getUniqueEmployeeDepartment();
}
