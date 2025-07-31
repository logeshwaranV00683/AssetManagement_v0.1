package com.verinite.assetmanagementtool.service.mailservice;

import com.verinite.assetmanagementtool.dto.AdminRegistrationDto;
import com.verinite.assetmanagementtool.entity.AdminRegistrationEntity;
import com.verinite.assetmanagementtool.repository.AdminRegistrationRepository;
import com.verinite.assetmanagementtool.service.serviceImpl.AdminServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
public class OTPMailer {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AdminRegistrationRepository adminRegistrationRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AdminServiceImpl adminService;

    public void sendOTP(AdminRegistrationDto adminRegistrationDto) throws MessagingException {
        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        AdminRegistrationEntity adminRegistrationEntity = adminRegistrationRepository.findByEmpId(adminRegistrationDto.getEmpId());
        adminRegistrationEntity.setOtp(otp);
        adminRegistrationEntity.setOtpVerify(true);
        adminRegistrationRepository.save(adminRegistrationEntity);
        new Thread(() -> {
            try {
                Thread.sleep(90000);
                AdminRegistrationEntity adminRegistrationEntityDB = adminRegistrationRepository.findByEmpId(adminRegistrationEntity.getEmpId());
                adminRegistrationEntityDB.setOtpVerify(false);
                adminRegistrationEntityDB.setOtp(adminService.generateComplexPassword(64));
                adminRegistrationRepository.save(adminRegistrationEntityDB);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        sendOtpMail(adminRegistrationDto.getFirstName(), adminRegistrationDto.getLastName(), adminRegistrationDto.getMail(), otp);
    }

    public void sendOtpMail(String firstName, String lastName, String email, String otp) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("Your OTP for Asset Management Verification");

        String content = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>OTP Verification</title>\n" +
                "</head>\n" +
                "<body style=\"margin: 0; box-sizing: border-box; font-family: 'Roboto', sans-serif; background: #00a3e8;\">\n" +
                "<div style=\"margin: 0 auto; width: 700px; background: #FFF; padding: 20px;\">\n" +
                "    <div style=\"border-bottom: 2px solid #00a63f; padding-bottom: 10px; display: flex; justify-content: space-between; align-items: center;\">\n" +
                "        <img src=\"https://raw.githubusercontent.com/g0bikrishnan/images/main/v.jpg\" alt=\"Logo\" style=\"width: 180px;\">\n" +
                "    </div>\n" +
                "    <div style=\"padding: 20px;\">\n" +
                "        <h2 style=\"font-size: 1.1em; color: #444;\">Hi " + firstName + " " + lastName + ",</h2>\n" +
                "        <p style=\"font-size: 0.95em; color: #666; line-height: 1.6em;\">You have requested to verify your identity for Asset Management. Please use the following OTP to proceed:</p>\n" +
                "        <div style=\"margin: 20px 0; font-size: 24px; background: #0072C6; color: white; padding: 15px; text-align: center; border-radius: 5px; letter-spacing: 4px;\">\n" +
                otp +
                "        </div>\n" +
                "        <p style=\"font-size: 0.9em; color: #999;\">This OTP is valid for the next 1 minutes. If you did not request this, please ignore this email or contact support immediately.</p>\n" +
                "        <br>\n" +
                "        <p style=\"font-size: 0.9em; color: #666;\">Regards,<br>IT Team</p>\n" +
                "    </div>\n" +
                "    <footer style=\"border-top: 1px solid #eeeeee; padding-top: 15px; text-align: center; font-size: 0.8em; color: #999;\">\n" +
                "        Contact us at <a href=\"mailto:support@verinite.com\" style=\"color: #00a63f; text-decoration: none;\">support@verinite.com</a>\n" +
                "    </footer>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";

        helper.setText(content, true);
        mailSender.send(message);

    }

}
