package com.verinite.assetmangementtool.controller;

import com.verinite.assetmangementtool.service.ForgotPasswordInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
