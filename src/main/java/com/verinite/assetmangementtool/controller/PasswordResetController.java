package com.verinite.assetmangementtool.controller;

import com.verinite.assetmangementtool.dto.ResetPasswordDTO;
import com.verinite.assetmangementtool.service.ForgotPasswordInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
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

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtpAndResetPassword(@RequestBody ResetPasswordDTO resetPassword) {
        return forgotPasswordInterface.checkOtp(resetPassword);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePasswordWithCurrent(@RequestBody ResetPasswordDTO changePassword) {
        return forgotPasswordInterface.changePassword(changePassword);
    }
}
