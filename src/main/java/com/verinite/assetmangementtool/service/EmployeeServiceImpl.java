package com.verinite.assetmangementtool.service;

import com.verinite.assetmangementtool.dto.EmployeeDto;
import com.verinite.assetmangementtool.entity.EmployeeEntity;
import com.verinite.assetmangementtool.repository.AdminRegistrationRepository;
import com.verinite.assetmangementtool.repository.EmployeeRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    EmployeeRepository employeeRepo;
    @Autowired
    AdminRegistrationRepository adminRegistrationRepository;
    @Autowired
    AdminServiceImpl adminServiceImpl;


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
            if (employee.getStatus() != null) {
                existingEmployee.setStatus(employee.getStatus());
            }

            if (employee.getRole() != null) {
                if(employee.getRole().equalsIgnoreCase("Admin"))
                {
                    if(!adminRegistrationRepository.existsByEmpId(employee.getEmpId()))
                    {
                    adminServiceImpl.registerNewAdminWithoutPassword(existingEmployee);
                    }
                    else
                    {
                        adminServiceImpl.updateAdminEntity(existingEmployee);
                    }
                }
                if(employee.getRole().equalsIgnoreCase("User"))
                {
                    if(existingEmployee.getRole().equalsIgnoreCase("Admin")) {
                        adminServiceImpl.deleteAdmin(existingEmployee.getEmpId());
                    }
                }
                existingEmployee.setRole(employee.getRole());
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

    public void exportEmployeesToExcel(OutputStream outputStream, String exportType, String filter) throws IOException {
        List<EmployeeEntity> employees;

        // Fetch employees based on export type and filter
        if ("Active".equalsIgnoreCase(exportType)) {
            employees = filter == null || filter.isEmpty()
                    ? employeeRepo.findByStatus("Active")
                    : employeeRepo.findByStatusAndFilter("Active", "%" + filter.toLowerCase() + "%");
        } else if ("Inactive".equalsIgnoreCase(exportType)) {
            employees = filter == null || filter.isEmpty()
                    ? employeeRepo.findByStatus("Inactive")
                    : employeeRepo.findByStatusAndFilter("Inactive", "%" + filter.toLowerCase() + "%");
        } else {
            employees = filter == null || filter.isEmpty()
                    ? employeeRepo.findAll()
                    : employeeRepo.findByFilter("%" + filter.toLowerCase() + "%");
        }

        // Create Excel workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Employees");

        // Create styles
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);

        // Create header row with styling
        String[] headers = {"Employee ID", "First Name", "Last Name", "Role", "Mail", "Mobile", "Location", "Status", "Department", "Designation"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Fill data rows with styling
        int rowNum = 1;
        for (EmployeeEntity employee : employees) {
            Row row = sheet.createRow(rowNum++);
            createDataCell(row, 0, employee.getEmpId(), dataStyle);
            createDataCell(row, 1, employee.getFirstName(), dataStyle);
            createDataCell(row, 2, employee.getLastName(), dataStyle);
            createDataCell(row, 3, employee.getRole(), dataStyle);
            createDataCell(row, 4, employee.getMail(), dataStyle);
            createDataCell(row, 5, employee.getMobile(), dataStyle);
            createDataCell(row, 6, employee.getLocation(), dataStyle);
            createDataCell(row, 7, employee.getStatus(), dataStyle);
            createDataCell(row, 8, employee.getDepartment(), dataStyle);
            createDataCell(row, 9, employee.getDesignation(), dataStyle);
        }

        // Auto-size all columns for better presentation
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write to the output stream
        workbook.write(outputStream);
        workbook.close();
    }


    // Helper method: Create a styled data cell
    private void createDataCell(Row row, int colIdx, String value, CellStyle style) {
        Cell cell = row.createCell(colIdx);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    // Helper method: Create a professional header style
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    // Helper method: Create a professional data style
    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }


}
