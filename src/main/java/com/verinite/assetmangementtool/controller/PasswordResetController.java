package com.verinite.assetmangementtool.controller;

import com.verinite.assetmangementtool.dto.ResetPasswordDTO;
import com.verinite.assetmangementtool.service.ForgotPasswordInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RestController
public class PasswordResetController {

    @Autowired
    ForgotPasswordInterface forgotPasswordInterface;

    @PostMapping("forgot_password/mail_valid")
    public ResponseEntity<?> mailValid(@RequestParam String mail) throws MessagingException, UnsupportedEncodingException {
        return forgotPasswordInterface.checkMail(mail);
    }

    @PostMapping("forgot_password/checkOTP")
    public ResponseEntity<?> checkOtp(@RequestBody ResetPasswordDTO resetPassword){
        return forgotPasswordInterface.checkOtp(resetPassword);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ResetPasswordDTO changePassword){
        return forgotPasswordInterface.changePassword(changePassword);
    }
}
