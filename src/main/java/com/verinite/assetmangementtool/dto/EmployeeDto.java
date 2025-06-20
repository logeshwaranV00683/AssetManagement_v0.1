package com.verinite.assetmangementtool.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
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
    @Schema(type = "string", format = "binary")
    private MultipartFile picture;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeDto e)) return false;
        return this.empId != null && e.empId!=null&&this.empId.equals(e.empId);
    }

    @Override
    public int hashCode() {
        return empId != null ? empId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "EmployeeDto{" +
                "empId='" + empId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role='" + role + '\'' +
                ", mail='" + mail + '\'' +
                ", mobile='" + mobile + '\'' +
                ", location='" + location + '\'' +
                ", status='" + status + '\'' +
                ", department='" + department + '\'' +
                ", designation='" + designation + '\'' +
                '}';
    }
}
