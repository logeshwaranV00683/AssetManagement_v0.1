package com.verinite.assetmanagementtool.service.serviceImpl;

import com.verinite.assetmanagementtool.dto.EmployeeDto;
import com.verinite.assetmanagementtool.dto.EmployeeExportDto;
import com.verinite.assetmanagementtool.entity.EmployeeEntity;
import com.verinite.assetmanagementtool.repository.AdminRegistrationRepository;
import com.verinite.assetmanagementtool.repository.EmployeeRepository;
import com.verinite.assetmanagementtool.service.AssignedAssetsService;
import com.verinite.assetmanagementtool.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    EmployeeRepository employeeRepo;
    @Autowired
    AdminRegistrationRepository adminRegistrationRepository;
    @Autowired
    AdminServiceImpl adminServiceImpl;
    @Autowired
    private Validator validator;
    @Autowired
    private AssignedAssetsService assignedAssetsService;


    public EmployeeDto saveEmployee(EmployeeDto employeeDTO) {
        EmployeeEntity employeeEntity = dtoToEntity(employeeDTO);

        employeeEntity.setRole("Employee");
        employeeEntity.setStatus("Active");
        // Check if the employee ID already exists
        if (employeeRepo.findByEmpId(employeeEntity.getEmpId()) == null) {
            if (!(employeeRepo.existsByMail(employeeEntity.getMail()) || employeeRepo.existsByMobile(employeeEntity.getMobile()))) {
                EmployeeEntity savedEntity = employeeRepo.save(employeeEntity);
                return entityToDto(savedEntity);
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Employee Mail or Mobile already exists");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Employee already exists");
        }
    }

    public void saveBulkEmployee(List<EmployeeDto> employeeDTOs) {

        List<EmployeeEntity> employeeEntities = employeeDTOs.stream().map(this::dtoToEntity)
                .toList();
        List<EmployeeEntity> newEmployees = employeeEntities.stream()
                .filter(employeeEntity -> employeeEntity.getEmpId() != null && !employeeEntity.getEmpId().isEmpty() && employeeRepo.findByEmpId(employeeEntity.getEmpId()) == null)
                .collect(Collectors.toList());
        List<EmployeeEntity> savedEntities = employeeRepo.saveAll(newEmployees);
//        return savedEntities.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    private EmployeeEntity dtoToEntity(EmployeeDto dto) {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setEmpId(dto.getEmpId());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setRole(dto.getRole());
        entity.setMail(dto.getMail());
        entity.setMobile(dto.getMobile());
        entity.setLocation(dto.getLocation());
        entity.setStatus(dto.getStatus());
        entity.setDepartment(dto.getDepartment());
        entity.setDesignation(dto.getDesignation());
        return entity;
    }

    private EmployeeDto entityToDto(EmployeeEntity entity) {
        EmployeeDto dto = new EmployeeDto();
        dto.setEmpId(entity.getEmpId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setRole(entity.getRole());
        dto.setMail(entity.getMail());
        dto.setMobile(entity.getMobile());
        dto.setLocation(entity.getLocation());
        dto.setStatus(entity.getStatus());
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
    @Transactional
    public ResponseEntity<?> deleteEmployeeById(String empId) {
        if (employeeRepo.existsById(empId)) {
            if (assignedAssetsService.getAllAssetsAssignedToParticularEmployee(empId).getStatusCode() == HttpStatus.OK) {
                return new ResponseEntity<>("Still Assets are not returned", HttpStatus.BAD_REQUEST);
            }
            adminRegistrationRepository.deleteByEmpId(empId);
            employeeRepo.deleteById(empId);
            return ResponseEntity.ok("Employee deleted successfully");
        } else {
            return new ResponseEntity<>("Employee not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> importEmployeeFromExcel(InputStream inputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook(inputStream);
        HashMap<String, String> data = importSheet(workbook);

        workbook.close();
        if (data == null) {
            return new ResponseEntity<>("Employee Sheet Not Found", HttpStatus.NOT_ACCEPTABLE);
        }
        return !data.isEmpty() ? ResponseEntity.ok(data) : ResponseEntity.ok("All Data Successfully Imported into Data Base");
    }

    private HashMap<String, String> importSheet(Workbook workbook) {
        Sheet sheet = workbook.getSheet("Employees");
        if (sheet == null) return null;

        Iterator<Row> rows = sheet.iterator();
        if (rows.hasNext()) rows.next();

        HashSet<EmployeeDto> employees = new HashSet<>();
        HashMap<String, String> skippedData = new HashMap<>();
        while (rows.hasNext()) {
            Row row = rows.next();
            EmployeeDto employee = new EmployeeDto();
            employee.setEmpId(getCellValue(row, 0));
            employee.setFirstName(getCellValue(row, 1));
            employee.setLastName(getCellValue(row, 2));
            employee.setMail(getCellValue(row, 4));
            employee.setMobile(getCellValue(row, 5));
            employee.setLocation(getCellValue(row, 6));
            employee.setDepartment(getCellValue(row, 8));
            employee.setDesignation(getCellValue(row, 9));
            employee.setStatus(getCellValue(row, 7));
            employee.setRole("Employee");
            Set<ConstraintViolation<EmployeeDto>> violations = validator.validate(employee);
            if (!violations.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (ConstraintViolation<EmployeeDto> v : violations) {
                    sb.append(v.getPropertyPath())
                            .append(": ")
                            .append(v.getMessage())
                            .append("; ");
                }
                skippedData.put(employee.getEmpId(), "Validation Failed: " + sb);
                continue;
            }
            if (employeeRepo.existsByMail(employee.getMail()) || employeeRepo.existsByMobile(employee.getMobile())) {
                log.warn("Duplicate Mail or Mobile found (ignored): {}", employee.getEmpId());
                skippedData.put(employee.getEmpId(), "Duplicate or Invalid Mail and Mobile found (ignored)");
                continue;
            }
            if (!employees.add(employee)) {
                log.warn("Duplicate employee found (ignored): {}", employee.getEmpId());
                skippedData.put(employee.getEmpId(), "Duplicate or Invalid employee found (ignored)");
            }
        }
        saveBulkEmployee(new LinkedList<>(employees));
        skippedData.entrySet().removeIf(entry -> entry.getKey() == null);
        return skippedData;
    }

    private String getCellValue(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex);
        return (cell != null) ? cell.toString().trim() : null;
    }


    public Object updateEmp(String empId, EmployeeDto employee) {
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
                if (employee.getStatus().equalsIgnoreCase("Inactive") && adminRegistrationRepository.existsByEmpId(employee.getEmpId())) {
                    existingEmployee.setStatus(employee.getStatus());
                    adminServiceImpl.deleteAdmin(existingEmployee.getEmpId());
                } else {
                    existingEmployee.setStatus(employee.getStatus());
                }
            }

            if (employee.getRole() != null) {
                if (employee.getRole().equalsIgnoreCase("Admin")) {
                    if (!adminRegistrationRepository.existsByEmpId(employee.getEmpId())) {
                        if (existingEmployee.getStatus().equalsIgnoreCase("active") && (employee.getStatus() == null || employee.getStatus().equalsIgnoreCase("active"))) {
                            adminServiceImpl.registerNewAdminWithoutPassword(existingEmployee);
                            existingEmployee.setRole(employee.getRole());
                        }
                    } else {
                        adminServiceImpl.updateAdminEntity(existingEmployee);
                    }
                }
                if (employee.getRole().equalsIgnoreCase("Employee")) {
                    if (existingEmployee.getRole().equalsIgnoreCase("Admin")) {
                        adminServiceImpl.deleteAdmin(existingEmployee.getEmpId());
                    }
                }
            }
            return employeeRepo.save(existingEmployee);
        } catch (Exception e) {
            return "Error updating record with ID: " + empId + ". Error: " + e.getMessage();
        }
    }

    @Override
    public List<EmployeeEntity> getActiveAccounts(String str) {
        return employeeRepo.findByIgnoreCaseStatus(str);
    }


    @Override
    public ResponseEntity<?> getAllByLocation(String str) {
        List<EmployeeEntity> employees = employeeRepo.findByIgnoreCaseLocation(str);
        if (employees.isEmpty()) {
            String message = "No employees found for location: " + str;
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(employees);
    }

    public ResponseEntity<?> getByIdForAdmin(String empId) {

        EmployeeEntity emp = employeeRepo.findByEmpId(empId);
        if (emp != null) {
            if (emp.getStatus().equalsIgnoreCase("Active")) {
                if (emp.getRole().equalsIgnoreCase("Employee")) {
                    return ResponseEntity.ok().body(emp);
                } else {
                    return ResponseEntity.status(HttpStatus.FOUND).body("Employee is Already Admin");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Employee not Found(Inactive)");

            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee is Not found 😥");
        }

    }

    @Override
    public void exportEmployeesToExcel(List<EmployeeExportDto> data, OutputStream outputStream) throws IOException {


        Workbook workbook = new XSSFWorkbook();

        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);

        createEmployeeSheet(workbook, data, headerStyle, dataStyle);

        workbook.write(outputStream);
        workbook.close();
    }

    private void createEmployeeSheet(Workbook workbook, List<EmployeeExportDto> employees,
                                     CellStyle headerStyle, CellStyle dataStyle) {

        Sheet sheet = workbook.createSheet("Employees");
        String[] headers = {"Employee ID", "First Name", "Last Name", "Role", "Mail", "Mobile", "Location", "Status", "Department", "Designation"};

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (EmployeeExportDto employee : employees) {
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

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void createDataCell(Row row, int colIdx, String value, CellStyle style) {
        Cell cell = row.createCell(colIdx);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

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

    public List<String> getUniqueEmployeeLocation() {
        return employeeRepo.getUniqueEmployeeLocation();
    }

    public List<String> getUniqueEmployeeDesignation() {
        return employeeRepo.getUniqueEmployeeDesignation();
    }

    public List<String> getUniqueEmployeeDepartment() {
        return employeeRepo.getUniqueEmployeeDepartment();
    }

}
