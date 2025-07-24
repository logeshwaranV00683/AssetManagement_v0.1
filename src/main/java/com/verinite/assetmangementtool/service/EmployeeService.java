package com.verinite.assetmangementtool.service;

import com.verinite.assetmangementtool.dto.EmployeeDto;
import com.verinite.assetmangementtool.dto.EmployeeExportDto;
import com.verinite.assetmangementtool.entity.EmployeeEntity;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface EmployeeService {
    // EmployeeEntity saveEmployee(EmployeeEntity employee);

    public EmployeeDto saveEmployee(EmployeeDto employeeDTO);

    List<EmployeeEntity> allEmployees();

    Object getById(String empId);

    Object deleteEmpByID(String empId);

    List<EmployeeEntity> getActiveAccounts(String str);

    List<EmployeeEntity> getAllByLocation(String str);

    Object updateEmp(String empId, EmployeeDto employee);

    void deleteEmployeeById(String empId);

    ResponseEntity<?> importEmployeeFromExcel(InputStream inputStream) throws IOException;

    void exportEmployeesToExcel(List<EmployeeExportDto> data, OutputStream outputStream) throws IOException;
}
