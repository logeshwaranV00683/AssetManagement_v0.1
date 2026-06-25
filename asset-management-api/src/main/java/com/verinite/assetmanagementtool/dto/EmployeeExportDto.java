package com.verinite.assetmanagementtool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeExportDto {

    private String empId;
    private String firstName;
    private String lastName;
    private String role;
    private String mail;
    private String mobile;
    private String location;
    private String status;
    private String department;
    private String designation;
}
