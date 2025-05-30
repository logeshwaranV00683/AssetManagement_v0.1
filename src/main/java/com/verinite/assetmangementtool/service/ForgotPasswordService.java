package com.verinite.assetmangementtool.service;

import com.verinite.assetmangementtool.dto.AdminRegistrationDto;
import com.verinite.assetmangementtool.dto.ResetPasswordDTO;
import com.verinite.assetmangementtool.entity.AdminRegistrationEntity;
import com.verinite.assetmangementtool.repository.AdminRegistrationRepository;
import com.verinite.assetmangementtool.service.mailservice.OTPMailer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private OTPMailer otpMailer;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public ResponseEntity<?> checkMail(String mail) throws MessagingException, UnsupportedEncodingException {
        AdminRegistrationDto adminRegistrationDto = modelMapper.map(adminRegistrationRepository.findByMail(mail),AdminRegistrationDto.class);
        if(adminRegistrationDto != null) {
            //otpService.sendOTP(adminRegistrationDto);
            otpMailer.sendOTP(adminRegistrationDto);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

    }

    @Override
    public ResponseEntity<?> checkOtp(ResetPasswordDTO resetPassword) {
        String mail = resetPassword.getMail();
        String otp = resetPassword.getOtp();
        String password = resetPassword.getNewPassword();

        AdminRegistrationEntity adminRegistrationEntity = adminRegistrationRepository.findByMail(mail).get();
        if (adminRegistrationEntity.getOtp().equals(otp)){
            adminRegistrationEntity.setPassword(bCryptPasswordEncoder.encode(password));
            adminRegistrationRepository.save(adminRegistrationEntity);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }




    @Override
    public ResponseEntity<?> changePassword(ResetPasswordDTO changePassword) {
        String mail = changePassword.getMail();
        String oldPassword = changePassword.getOldPassword();
        String newPassword = changePassword.getNewPassword();

        AdminRegistrationEntity adminRegistrationEntity = adminRegistrationRepository.findByMail(mail).get();
        if (bCryptPasswordEncoder.matches(oldPassword,adminRegistrationEntity.getPassword())){
            adminRegistrationEntity.setPassword(newPassword);
            adminRegistrationRepository.save(adminRegistrationEntity);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}
