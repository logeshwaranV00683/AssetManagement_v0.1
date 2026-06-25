package com.verinite.assetmanagementtool.controller;

import com.verinite.assetmanagementtool.dto.ResetPasswordDTO;
import com.verinite.assetmanagementtool.service.ForgotPasswordInterface;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/reset-password")
public class PasswordResetController {

    @Autowired
    private ForgotPasswordInterface forgotPasswordInterface;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtpToEmail(@RequestParam String empId)
            throws MessagingException, UnsupportedEncodingException {
        return forgotPasswordInterface.sendOTPMail(empId);
    }

    @Operation(summary = "Verify the otp and reset the password, required fields are new Password, otp and mail | Login Not Required")
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtpAndResetPassword(@RequestBody @Valid ResetPasswordDTO resetPassword) {
        return forgotPasswordInterface.checkOtp(resetPassword);
    }

    @Operation(summary = "Change the password using only old password without otp, require fields are new password, old password and mail | Login is Required")
    @PostMapping("/change-password-using-oldPassword")
    public ResponseEntity<?> changePasswordWithCurrent(@RequestBody @Valid ResetPasswordDTO changePassword) {
        return forgotPasswordInterface.changePassword(changePassword);
    }
}
