package com.verinite.assetmangementtool.service;

import com.verinite.assetmangementtool.dto.AdminRegistrationDto;
import com.verinite.assetmangementtool.repository.AdminRegistrationRepository;
import com.verinite.assetmangementtool.service.mailservice.OTPMailer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Service
public class ForgotPasswordService implements ForgotPasswordInterface {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AdminRegistrationRepository adminRegistrationRepository;

    @Autowired
    OtpService otpService;

    @Override
    public ResponseEntity<?> checkMail(String mail) throws MessagingException, UnsupportedEncodingException {
        AdminRegistrationDto adminRegistrationDto = modelMapper.map(adminRegistrationRepository.findByMail(mail),AdminRegistrationDto.class);
        if(adminRegistrationDto != null) {
            otpService.sendOTP(adminRegistrationDto);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

    }
}
