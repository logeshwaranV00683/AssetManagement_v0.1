package com.verinite.assetmanagementtool.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Entity
@Table(name = "tbl_admin")
public class AdminRegistrationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_Id", nullable = false, unique = true)
    private Long adminId;
    @NotBlank
    @Pattern(regexp = "^(?i)[a-z]+(?:[ '-][a-z]+)*$", message = "First Name must contain only letters, spaces, or hyphens")
    @Column(name = "firstName")
    private String firstName;
    @NotBlank
    @Pattern(regexp = "^(?i)[a-z]+(?:[ '-][a-z]+)*$", message = "Last Name must contain only letters, spaces, or hyphens")
    @Column(name = "lastName")
    private String lastName;
    @NotBlank
    @Column(name = "empId")
    private String empId;
    @Email
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email address")
    @NotBlank
    private String mail;
    @NotBlank
    @Pattern(regexp = "^(?i)(Active|Inactive)?$", message = "status can be Active or Inactive")
    private String status;
    @NotBlank
    @Pattern(regexp = "^(?i)(Employee|Admin)?$", message = "Role can be Admin or Employee")
    private String role;
    @NotBlank
    @Pattern(regexp = "^(?i)[a-z]+(?:[ '-][a-z]+)*$", message = "Location must contain only letters, spaces, or hyphens")
    private String location;
    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;
    @Pattern(regexp = "^\\d+$", message = "Only number format will ne accepted")
    private String otp;
    private boolean otpVerify;
}
