package com.verinite.assetmanagementtool.controller;

import com.verinite.assetmanagementtool.dto.EmployeeDto;
import com.verinite.assetmanagementtool.dto.EmployeeExportDto;
import com.verinite.assetmanagementtool.entity.EmployeeEntity;
import com.verinite.assetmanagementtool.service.serviceImpl.EmployeeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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

    @PostMapping("employee/saveemployee")
    public ResponseEntity<?> saveEmployee(@RequestBody @Valid @Validated(NotBlank.class) EmployeeDto employeeDTO) {
        try {
            EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getReason());
        }
    }

    @GetMapping("employee/employeelist")
    public List<EmployeeEntity> allEmployee() {

        return employeeService.allEmployees();
    }

    @GetMapping("/getEmployee/{empId}")
    public ResponseEntity<?> getById(@RequestParam String empId) {
        return employeeService.getById(empId);
    }

    @DeleteMapping("/deleteEmp/{empId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable String empId) {
        String message = employeeService.deleteEmployeeById(empId);
        return ResponseEntity.ok(message);
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

    @GetMapping("/location/{locationName}")
    public ResponseEntity<?> getByLocations(@PathVariable String locationName) {
        return employeeService.getAllByLocation(locationName);
    }

    @GetMapping("/employee/get/for/admin/{empId}")
    public ResponseEntity<?> getUserForAdmin(@PathVariable String empId) {
        return employeeService.getByIdForAdmin(empId);
    }
}
