package com.verinite.assetmangementtool.service;

import com.verinite.assetmangementtool.dto.EmployeeDto;
import com.verinite.assetmangementtool.entity.EmployeeEntity;

import java.util.List;

public interface EmployeeService {
	// EmployeeEntity saveEmployee(EmployeeEntity employee);

	public EmployeeDto saveEmployee(EmployeeDto employeeDTO);

	List<EmployeeEntity> allEmployees();

	Object getById(String empId);

	Object deleteEmpByID(String empId);

	List<EmployeeEntity> getActiveAccounts(String str);

	List<EmployeeEntity> getAllByLocation(String str);

	Object updateEmp(String empId, EmployeeEntity employee);

	void deleteEmployeeById(String empId);

}
