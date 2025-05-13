package com.verinite.assetmangementtool.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdminRegistrationDto implements Serializable {
    private final Long adminId = null;
    private final String firstName = "";
    private final String lastName = "";
    private final String empId = "";
    private final String mail = "";
    private final String role = "";
    private final String location = "";
}
