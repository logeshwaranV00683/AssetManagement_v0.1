package com.verinite.assetmangementtool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeDto)) return false;
        EmployeeDto e = (EmployeeDto) o;
        return empId != null && empId.equals(e.empId);
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
