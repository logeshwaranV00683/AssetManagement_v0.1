//package com.verinite.assetmangementtool.service;
//
//import com.verinite.assetmangementtool.dto.AdminRegistrationDto;
//import com.verinite.assetmangementtool.entity.AdminRegistrationEntity;
//import com.verinite.assetmangementtool.repository.AdminRegistrationRepository;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Random;
//
//@Service
//public class OtpService {
//
//        @Autowired
//        private AdminRegistrationRepository adminRegistrationRepository;
//
//        @Autowired
//        private ModelMapper modelMapper;
//
//        public void sendOTP(AdminRegistrationDto adminRegistrationDto) {
//            String otp = String.valueOf(100000 + new Random().nextInt(900000));
//            adminRegistrationDto.setOtp(otp);
//            adminRegistrationDto.setOtpVerify(false);
//            AdminRegistrationEntity adminRegistrationEntity= modelMapper.map(adminRegistrationDto, AdminRegistrationEntity.class);
//            adminRegistrationRepository.save(adminRegistrationEntity);
//            String subject = "OTP for Password Reset";
//            String body = "Your OTP is: " + otp;
//            sendEmail(adminRegistrationDto.getMail(), subject, body);
//        }
//
//        public boolean verifyOTP(String email, String otp) {
//           AdminRegistrationEntity adminRegistrationEntity = adminRegistrationRepository.findBymail(email);
//            if (adminRegistrationEntity != null && adminRegistrationEntity.getOtp().equals(otp)) {
//                adminRegistrationEntity.setOtpVerify(true);
//                adminRegistrationEntity.setOtp(null);
//                adminRegistrationRepository.save(adminRegistrationEntity);
//                return true;
//            }
//            return false;
//        }
//}
