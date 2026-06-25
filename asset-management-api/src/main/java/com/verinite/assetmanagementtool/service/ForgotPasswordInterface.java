package com.verinite.assetmanagementtool.service;

import com.verinite.assetmanagementtool.dto.ResetPasswordDTO;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;


public interface ForgotPasswordInterface {

    ResponseEntity<?> sendOTPMail(String empId) throws MessagingException, UnsupportedEncodingException;

    ResponseEntity<?> checkOtp(ResetPasswordDTO resetPassword);

    ResponseEntity<?> changePassword(ResetPasswordDTO changePassword);
}
