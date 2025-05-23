package com.verinite.assetmangementtool.controller;

import com.verinite.assetmangementtool.entity.AdminRegistrationEntity;
import com.verinite.assetmangementtool.repository.AdminRegistrationRepository;
import com.verinite.assetmangementtool.service.OtpService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class OtpController {

        @Autowired
        private OtpService otpService;

        @Autowired
        private AdminRegistrationRepository adminRegistrationRepository;

        public String verifyOtp(@RequestParam String email, @RequestParam String otp) {
            boolean valid = otpService.verifyOTP(email, otp);
            return valid ? "OTP Verified. You can now reset your password." : "Invalid OTP.";
        }

        public String resetPassword(@RequestParam String mail,
                                    @RequestParam String newPassword) {
            AdminRegistrationEntity adminRegistrationEntity = adminRegistrationRepository.findBymail(mail);
            if (adminRegistrationEntity == null || !adminRegistrationEntity.isOtpVerify()) {
                return "OTP not verified.";
            }
            adminRegistrationEntity.setPassword(newPassword);
            adminRegistrationEntity.setOtpVerify(false); // reset flag
            adminRegistrationRepository.save(adminRegistrationEntity);
            return "Password reset successful.";
        }
}
