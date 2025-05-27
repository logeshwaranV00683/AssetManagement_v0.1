package com.verinite.assetmangementtool.dto;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class AdminRegistrationDto implements Serializable {
    private Long adminId;
    private String firstName;
    private String lastName;
    private String empId;
    private String mail;
    private String role;
    private String location;
    private String otp;

}
