package com.verinite.assetmanagementtool.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class AdminLoginDto {
    @Pattern(regexp = "^(V\\d{5})$", message = "Must be V followed by 5 digits")
    private String empId;
    @NotBlank
    private String password;
}
