package com.verinite.assetmanagementtool.service.serviceImpl;

import com.verinite.assetmanagementtool.dto.AdminRegistrationDto;
import com.verinite.assetmanagementtool.dto.ResetPasswordDTO;
import com.verinite.assetmanagementtool.entity.AdminRegistrationEntity;
import com.verinite.assetmanagementtool.repository.AdminRegistrationRepository;
import com.verinite.assetmanagementtool.service.ForgotPasswordInterface;
import com.verinite.assetmanagementtool.service.mailservice.OTPMailer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ForgotPasswordService implements ForgotPasswordInterface {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AdminRegistrationRepository adminRegistrationRepository;

    @Autowired
    private OTPMailer otpMailer;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public ResponseEntity<?> sendOTPMail(String empId) throws MessagingException {
        AdminRegistrationDto adminRegistrationDto = modelMapper.map(adminRegistrationRepository.findByEmpId(empId), AdminRegistrationDto.class);
        if (adminRegistrationDto != null) {
            otpMailer.sendOTP(adminRegistrationDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public ResponseEntity<?> checkOtp(ResetPasswordDTO resetPassword) {
        String mail = resetPassword.getMail();
        String otp = resetPassword.getOtp();
        String password = resetPassword.getNewPassword();

        if (otp == null || otp.isEmpty() || password == null || password.isEmpty()) {
            return new ResponseEntity<>("OTP or password cannot be empty", HttpStatus.BAD_REQUEST);
        }

        Optional<AdminRegistrationEntity> optionalAdmin = adminRegistrationRepository.findByMail(mail);
        if (optionalAdmin.isEmpty()) {
            return new ResponseEntity<>("Employee not found", HttpStatus.NOT_FOUND);
        }

        AdminRegistrationEntity admin = optionalAdmin.get();

        if (!otp.equals(admin.getOtp())) {
            return new ResponseEntity<>("Invalid OTP", HttpStatus.BAD_REQUEST);
        }

        if (!admin.isOtpVerify()) {
            return new ResponseEntity<>("OTP not verified. Please verify the OTP before resetting password.", HttpStatus.FORBIDDEN);
        }

        admin.setPassword(bCryptPasswordEncoder.encode(password));
        admin.setOtp(null);
        admin.setOtpVerify(false);
        adminRegistrationRepository.save(admin);

        return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Map<String, String>> changePassword(ResetPasswordDTO changePassword) {
        Map<String, String> response = new HashMap<>();

        String mail = changePassword.getMail();
        String oldPassword = changePassword.getOldPassword();
        String newPassword = changePassword.getNewPassword();

        Optional<AdminRegistrationEntity> optionalAdmin = adminRegistrationRepository.findByMail(mail);

        if (optionalAdmin.isEmpty()) {
            response.put("message", "Employee not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        AdminRegistrationEntity admin = optionalAdmin.get();

        if (bCryptPasswordEncoder.matches(oldPassword, admin.getPassword())) {
            String encodedNewPassword = bCryptPasswordEncoder.encode(newPassword);
            admin.setPassword(encodedNewPassword);
            admin.setOtpVerify(false);
            adminRegistrationRepository.save(admin);

            response.put("message", "Password updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        response.put("message", "Old password is incorrect");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
