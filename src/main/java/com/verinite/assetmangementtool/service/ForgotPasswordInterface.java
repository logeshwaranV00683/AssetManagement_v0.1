package com.verinite.assetmangementtool.service;

import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;


public interface ForgotPasswordInterface {

    public ResponseEntity<?> checkMail(String mail) throws MessagingException, UnsupportedEncodingException;
}
