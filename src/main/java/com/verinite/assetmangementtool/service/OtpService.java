package com.verinite.assetmangementtool.service;

import com.verinite.assetmangementtool.dto.AdminRegistrationDto;
import com.verinite.assetmangementtool.entity.AdminRegistrationEntity;
import com.verinite.assetmangementtool.repository.AdminRegistrationRepository;
import com.verinite.assetmangementtool.service.mailservice.OTPMailer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
public class OtpService {

        @Autowired
        private AdminRegistrationRepository adminRegistrationRepository;

        @Autowired
        private ModelMapper modelMapper;

        @Autowired
        private OTPMailer otpMailer;

        public void sendOTP(AdminRegistrationDto adminRegistrationDto) throws MessagingException, UnsupportedEncodingException {
            String otp = String.valueOf(100000 + new Random().nextInt(900000));
            adminRegistrationDto.setOtp(otp);
            adminRegistrationDto.setOtpVerify(false);
            AdminRegistrationEntity adminRegistrationEntity= modelMapper.map(adminRegistrationDto, AdminRegistrationEntity.class);
            adminRegistrationRepository.save(adminRegistrationEntity);
            String subject = "OTP for Password Reset";
            String body = "Your OTP is: " + otp;
            otpMailer.mailAuthenticator(adminRegistrationDto.getFirstName(),adminRegistrationDto.getMail(),adminRegistrationDto.getEmpId());
        }

        public boolean verifyOTP(String email, String otp) {
           AdminRegistrationEntity adminRegistrationEntity = adminRegistrationRepository.findBymail(email);
            if (adminRegistrationEntity != null && adminRegistrationEntity.getOtp().equals(otp)) {
                adminRegistrationEntity.setOtpVerify(true);
                adminRegistrationEntity.setOtp(null);
                adminRegistrationRepository.save(adminRegistrationEntity);
                return true;
            }
            return false;
        }
}
