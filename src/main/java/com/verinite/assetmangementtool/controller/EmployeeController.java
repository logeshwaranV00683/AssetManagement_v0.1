package com.verinite.assetmangementtool.controller;

import com.verinite.assetmangementtool.dto.EmployeeDto;
import com.verinite.assetmangementtool.entity.EmployeeEntity;
import com.verinite.assetmangementtool.service.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
//@CrossOrigin(origins = "https://asset-tool.netlify.app", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/assetManager/v1/")
public class EmployeeController {
	@Autowired
	EmployeeServiceImpl employeeService;

//	@PostMapping("employee/saveemployee")
//	public EmployeeDto saveEmployee(@RequestBody EmployeeEntity employee) {
//		// logger.info("debug");
//
//		return employeeService.saveEmployee(null);
//	}
	@PostMapping("employee/saveemployee")
	public ResponseEntity<EmployeeDto> saveEmployee(@RequestBody EmployeeDto employeeDTO) {
		try {
			EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDTO);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatus()).body(null);
		}
	}

	@PostMapping("employee/save/bulk")
	public ResponseEntity<List<EmployeeDto>> saveEmployees(@RequestBody List<EmployeeDto> employeeDTOs) {
		try {
			List<EmployeeDto> savedEmployees = employeeService.saveBulkEmployee(employeeDTOs);
			return new ResponseEntity<>(savedEmployees, HttpStatus.CREATED);
		} catch (Exception e) {
			// Handle exceptions (like if there is an error with saving the employees)
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("employee/employeelist")
	public List<EmployeeEntity> allEmployee() {

		return employeeService.allEmployees();
	}

	@GetMapping("/getEmployee/{empId}")
	public Object getById(@PathVariable String empId) {

		return employeeService.getById(empId);
	}
//    @DeleteMapping("deleteEmp/{empId}")
//    public void deleteEmpById(@PathVariable String empId) {
//
//        return employeeService.deleteEmployeeById(empId);
//    }

	@DeleteMapping("/deleteEmp/{empId}")
	public ResponseEntity<EmployeeEntity> deleteEmployeeById(@PathVariable String empId) {
		employeeService.deleteEmployeeById(empId);
		return new ResponseEntity<>(HttpStatus.OK);

	}

	@GetMapping("/export/employees")
	public ResponseEntity<byte[]> exportEmployees() {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			// Generate Excel file content in memory
			employeeService.exportEmployeesToExcel(out);

			// Set response headers for file download
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", "employees.xlsx");

			return ResponseEntity.ok()
					.headers(headers)
					.body(out.toByteArray());
		} catch (IOException e) {
			return ResponseEntity.internalServerError()
					.body(("Error generating Excel file: " + e.getMessage()).getBytes());
		}
	}

	@PutMapping("/updateEmp/{empId}")
	public ResponseEntity<?> updateEmployee(@PathVariable String empId, @RequestBody EmployeeEntity employee) {
		try {
			Object result = employeeService.updateEmp(empId, employee);
			if (result instanceof EmployeeEntity) {
				return ResponseEntity.ok(result);
			} else {
				return ResponseEntity.badRequest().body(result);
			}
		} catch (Exception e) {
			return ResponseEntity.status(500).body("An error occurred while updating the employee: " + e.getMessage());
		}
	}

	@GetMapping("/employee/status/{checkStatus}")
	public List<EmployeeEntity> getActiveAccounts(@PathVariable String checkStatus) {

		return employeeService.getActiveAccounts(checkStatus);
	}

	@GetMapping("/location/{name}")
	public List<EmployeeEntity> getByLocations(@PathVariable String name) {
		return employeeService.getAllByLocation(name);
	}

	@GetMapping("/employee/get/for/admin/{empId}")
	public ResponseEntity<?> getUserForAdmin(@PathVariable String empId) {
		return employeeService.getByIdForAdmin(empId);
	}
}
