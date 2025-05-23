package com.verinite.assetmangementtool.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Data
@Entity
@Table(name = "tbl_admin")
public class AdminRegistrationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_Id", nullable = false, unique = true)
    private Long adminId;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "empId")
    private String empId;
    @Email
    private String mail;
    private String status;
    private String role;
    private String location;
    @Column(name = "password", nullable = false)
    private String password;
    private String otp;
    private boolean otpVerify;
}
