package com.verinite.assetmangementtool.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class AdminLoginDto {
    @Pattern(regexp = "^(V\\d{5})$", message = "Must be V followed by 5 digits")
    private String empId;
    private String password;
}
