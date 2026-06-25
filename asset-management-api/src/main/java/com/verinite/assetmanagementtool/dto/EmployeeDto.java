package com.verinite.assetmanagementtool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    @Pattern(regexp = "^(V\\d{5})?$", message = "Must be V followed by 5 digits or empty")
    private String empId;
    @NotBlank(groups = NotBlank.class)
    @Pattern(regexp = "^(?i)[a-z]+(?:[ '-][a-z]+)*$", message = "First Name must contain only letters, spaces, or hyphens")
    private String firstName;
    @NotBlank(groups = NotBlank.class)
    @Pattern(regexp = "^(?i)[a-z]+(?:[ '-][a-z]+)*$", message = "Last Name must contain only letters, spaces, or hyphens")
    private String lastName;
    @NotBlank(groups = NotBlank.class)
    @Pattern(regexp = "^(?i)(Employee|Admin)?$", message = "Role can be Admin or Employee")
    private String role;
    @NotBlank(groups = NotBlank.class)
    @Email
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@([A-Za-z_]+\\.)[A-Za-z]{2,3}$", message = "Invalid email address")
    private String mail;
    @NotBlank(groups = NotBlank.class)
    @Pattern(regexp = "^(0|\\+91)?[6-9]\\d{9}$", message = "Valid Mobile Number Needed")
    private String mobile;
    @NotBlank(groups = NotBlank.class)
    @Pattern(regexp = "^(?i)[a-z]+(?:[ '-][a-z]+)*$", message = "Location must contain only letters, spaces, or hyphens")
    private String location;
    @NotBlank(groups = NotBlank.class)
    @Pattern(regexp = "^(?i)(Active|Inactive)?$", message = "status can be Active or Inactive")
    private String status;
    @NotBlank(groups = NotBlank.class)
    @Pattern(regexp = "^(?i)[a-z]+(?:[ '-][a-z]+)*$", message = "Department must contain only letters, spaces, or hyphens")
    private String department;
    @NotBlank(groups = NotBlank.class)
    @Pattern(regexp = "^(?i)[a-z]+(?:[ '-][a-z]+)*$", message = "Designation must contain only letters, spaces, or hyphens")
    private String designation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeDto e)) return false;
        return this.empId != null && e.empId != null && this.empId.equals(e.empId);
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
