package com.verinite.assetmangementtool.controller;

import com.verinite.assetmangementtool.dto.EmployeeDto;
import com.verinite.assetmangementtool.dto.EmployeeExportDto;
import com.verinite.assetmangementtool.entity.EmployeeEntity;
import com.verinite.assetmangementtool.service.serviceImpl.EmployeeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    public ResponseEntity<EmployeeDto> saveEmployee(@RequestBody @Valid EmployeeDto employeeDTO) {
        try {
            EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatus()).body(null);
        }
    }

//    @PostMapping("employee/save/bulk")
//    public ResponseEntity<List<EmployeeDto>> saveEmployees(@RequestBody  @NotEmpty(message = "Employee list cannot be empty") List<@Valid EmployeeDto> employeeDTOs) {
//        try {
//            List<EmployeeDto> savedEmployees = employeeService.saveBulkEmployee(employeeDTOs);
//            return new ResponseEntity<>(savedEmployees, HttpStatus.CREATED);
//        } catch (Exception e) {
//            // Handle exceptions (like if there is an error with saving the employees)
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

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

    @PostMapping("employee/export")
    public ResponseEntity<byte[]> exportData(@RequestBody List<EmployeeExportDto> filteredRows) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            employeeService.exportEmployeesToExcel(filteredRows, out);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

            String fileName = URLEncoder.encode("exported_employees.xlsx", StandardCharsets.UTF_8);
            headers.setContentDisposition(ContentDisposition.attachment().filename(fileName).build());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(out.toByteArray());

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(("Error generating Excel file: " + e.getMessage()).getBytes());
        }
    }

    @Operation(summary = "Import Excel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful import")
    })
    @PostMapping(value = "employee/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importExcel(
            @Parameter(description = "Excel file", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestParam("file") MultipartFile file) {
        try {
            return employeeService.importEmployeeFromExcel(file.getInputStream());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }


    @PutMapping("/updateEmp/{empId}")
    public ResponseEntity<?> updateEmployee(@PathVariable String empId, @RequestBody @Valid EmployeeDto employee) {
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
