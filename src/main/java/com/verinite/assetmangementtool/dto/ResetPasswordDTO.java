package com.verinite.assetmangementtool.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDTO {
    private String mail;
    private String oldPassword;
    private String newPassword;
    private String otp;
}
