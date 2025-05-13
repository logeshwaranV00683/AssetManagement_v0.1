package com.verinite.assetmangementtool.service;

import com.verinite.assetmangementtool.dto.EmployeeDto;
import com.verinite.assetmangementtool.entity.EmployeeEntity;
import com.verinite.assetmangementtool.repository.EmployeeRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	EmployeeService employeeService;
	@Autowired
	EmployeeRepository employeeRepo;

//	public EmployeeDto saveEmployee(EmployeeDto employeeDTO) {
//		EmployeeEntity employeeEntity = dtoToEntity(employeeDTO);
//
//		// Set default values
//		employeeEntity.setRole("User"); // Set role to 'user'
//		employeeEntity.setStatus("Active"); // Set status to 'active'
//
//		// Generate employee ID
//		String lastEmpId = employeeRepo.findTopByOrderByEmpIdDesc().getEmpId();
//		String newEmpId = generateNewEmpId(lastEmpId);
//		employeeEntity.setEmpId(newEmpId);
//
//		if (employeeRepo.findByEmpId(employeeEntity.getEmpId()) == null) {
//			EmployeeEntity savedEntity = employeeRepo.save(employeeEntity);
//			return entityToDto(savedEntity);
//		} else {
//			throw new ResponseStatusException(HttpStatus.CONFLICT, "Employee already exists");
//		}
//	}
//
//	private String generateNewEmpId(String lastEmpId) {
//		if (lastEmpId == null) {
//			return "V00001";
//		}
//		int newId = Integer.parseInt(lastEmpId.substring(1)) + 1;
//		return String.format("V%05d", newId);
//	}

	public EmployeeDto saveEmployee(EmployeeDto employeeDTO) {
	    EmployeeEntity employeeEntity = dtoToEntity(employeeDTO);

	    // Set default values
	    employeeEntity.setRole("User"); // Set role to 'User'
	    employeeEntity.setStatus("Active"); // Set status to 'Active'

	    // Generate employee ID
	    EmployeeEntity lastEmployee = employeeRepo.findTopByOrderByEmpIdDesc();
	    String lastEmpId = (lastEmployee != null) ? lastEmployee.getEmpId() : "emp000";
	    String newEmpId = generateNewEmpId(lastEmpId);
	    employeeEntity.setEmpId(newEmpId);

	    // Check if the employee ID already exists
	    if (employeeRepo.findByEmpId(employeeEntity.getEmpId()) == null) {
	        EmployeeEntity savedEntity = employeeRepo.save(employeeEntity);
	        return entityToDto(savedEntity);
	    } else {
	        throw new ResponseStatusException(HttpStatus.CONFLICT, "Employee already exists");
	    }
	}

	// Example of generateNewEmpId method
	private String generateNewEmpId(String lastEmpId) {
	    int num = Integer.parseInt(lastEmpId.substring(3)) + 1;
	    return String.format("V%05d", num);
	}

	public List<EmployeeDto> saveBulkEmployee(List<EmployeeDto> employeeDTOs) {
		// Convert the list of DTOs to a list of entities
		List<EmployeeEntity> employeeEntities = employeeDTOs.stream().map(this::dtoToEntity)
				.collect(Collectors.toList());

		// Check for existing employees and separate them
		List<EmployeeEntity> newEmployees = employeeEntities.stream()
				.filter(employeeEntity -> employeeRepo.findByEmpId(employeeEntity.getEmpId()) == null)
				.collect(Collectors.toList());

		// Save the new employees
		List<EmployeeEntity> savedEntities = employeeRepo.saveAll(newEmployees);

		// Convert saved entities to DTOs
		return savedEntities.stream().map(this::entityToDto).collect(Collectors.toList());
	}

	private EmployeeEntity dtoToEntity(EmployeeDto dto) {
		EmployeeEntity entity = new EmployeeEntity();
		// entity.setEmpId(dto.getEmpId());
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		// entity.setRole(dto.getRole());
		entity.setMail(dto.getMail());
		entity.setMobile(dto.getMobile());
		entity.setLocation(dto.getLocation());
		// entity.setStatus(dto.getStatus());
		entity.setDepartment(dto.getDepartment());
		entity.setDesignation(dto.getDesignation());
		return entity;
	}

	private EmployeeDto entityToDto(EmployeeEntity entity) {
		EmployeeDto dto = new EmployeeDto();
		// dto.setEmpId(entity.getEmpId());
		dto.setFirstName(entity.getFirstName());
		dto.setLastName(entity.getLastName());
		// dto.setRole(entity.getRole());
		dto.setMail(entity.getMail());
		dto.setMobile(entity.getMobile());
		dto.setLocation(entity.getLocation());
		// dto.setStatus(entity.getStatus());
		dto.setDepartment(entity.getDepartment());
		dto.setDesignation(entity.getDesignation());
		return dto;
	}

	@Override
	public List<EmployeeEntity> allEmployees() {
return employeeRepo.findAllByOrderByEmpIdDesc();
	}

	@Override
	public ResponseEntity<?> getById(String empId) {

		EmployeeEntity emp = employeeRepo.findByEmpId(empId);
		if (emp != null) {
			return ResponseEntity.ok().body(emp);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");
		}
	}

	@Override
	public Object deleteEmpByID(String empId) {
		EmployeeEntity employee1 = (EmployeeEntity) employeeRepo.findByEmpId(empId);
		employee1.setStatus("inactive");
		employeeRepo.save(employee1);
		return new EmployeeEntity();
	}

	@Override
	public void deleteEmployeeById(String empId) {
		EmployeeEntity byEmpId = employeeRepo.findByEmpId(empId);
		employeeRepo.deleteById(empId);
	}


	public Object updateEmp(String empId, EmployeeEntity employee) {
		try {
			EmployeeEntity existingEmployee = employeeRepo.findByEmpId(empId);
			if (existingEmployee == null) {
				return "Record not found for ID: " + empId;
			}
			if (employee.getDepartment() != null) {
				existingEmployee.setDepartment(employee.getDepartment());
			}
			if (employee.getDesignation() != null) {
				existingEmployee.setDesignation(employee.getDesignation());
			}
			if (employee.getFirstName() != null) {
				existingEmployee.setFirstName(employee.getFirstName());
			}
			if (employee.getLastName() != null) {
				existingEmployee.setLastName(employee.getLastName());
			}
			if (employee.getLocation() != null) {
				existingEmployee.setLocation(employee.getLocation());
			}
			if (employee.getMail() != null) {
				existingEmployee.setMail(employee.getMail());
			}
			if (employee.getMobile() != null) {
				existingEmployee.setMobile(employee.getMobile());
			}
			if (employee.getRole() != null) {
				existingEmployee.setRole(employee.getRole());
			}
			if (employee.getStatus() != null) {
				existingEmployee.setStatus(employee.getStatus());
			}
			return employeeRepo.save(existingEmployee);
		} catch (Exception e) {
			// Log the exception and return error message
			return "Error updating record with ID: " + empId + ". Error: " + e.getMessage();
		}
	}

	@Override
	public List<EmployeeEntity> getActiveAccounts(String str) {
		return employeeRepo.findByIgnoreCaseStatus(str);
	}

//	

	@Override
	public List<EmployeeEntity> getAllByLocation(String str) {
		return employeeRepo.findByIgnoreCaseLocation(str);
	}

	public ResponseEntity<?> getByIdForAdmin(String empId) {

		EmployeeEntity emp = employeeRepo.findByEmpId(empId);
		if (emp != null) {
			if (emp.getStatus().equalsIgnoreCase("Active")) {
				if (emp.getRole().equalsIgnoreCase("User")) {
					return ResponseEntity.ok().body(emp);
				} else {
					return ResponseEntity.status(HttpStatus.FOUND).body("User is Already Admin");
				}
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not Found(Inactive)");

			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee is Not found 😥");
		}

	}
	public void exportEmployeesToExcel(OutputStream outputStream) throws IOException {
		List<EmployeeEntity> employees = employeeRepo.findAll();

		// Create Excel workbook and sheet
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Employees");

		// Create header row
		Row headerRow = sheet.createRow(0);
		String[] headers = {"Employee ID", "First Name", "Last Name", "Role", "Mail", "Mobile", "Location", "Status", "Department", "Designation"};
		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
		}

		// Fill data rows
		int rowNum = 1;
		for (EmployeeEntity employee : employees) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(employee.getEmpId());
			row.createCell(1).setCellValue(employee.getFirstName());
			row.createCell(2).setCellValue(employee.getLastName());
			row.createCell(3).setCellValue(employee.getRole());
			row.createCell(4).setCellValue(employee.getMail());
			row.createCell(5).setCellValue(employee.getMobile());
			row.createCell(6).setCellValue(employee.getLocation());
			row.createCell(7).setCellValue(employee.getStatus());
			row.createCell(8).setCellValue(employee.getDepartment());
			row.createCell(9).setCellValue(employee.getDesignation());
		}

		// Write to the output stream
		workbook.write(outputStream);
		workbook.close();
	}
}
