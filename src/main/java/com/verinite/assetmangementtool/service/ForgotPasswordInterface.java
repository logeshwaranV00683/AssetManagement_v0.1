package com.verinite.assetmangementtool.service;

import com.verinite.assetmangementtool.dto.ResetPasswordDTO;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;


public interface ForgotPasswordInterface {

    public ResponseEntity<?> sendOTPMail(String empId) throws MessagingException, UnsupportedEncodingException;

    public ResponseEntity<?> checkOtp(ResetPasswordDTO resetPassword);

    ResponseEntity<?> changePassword(ResetPasswordDTO changePassword);
}
