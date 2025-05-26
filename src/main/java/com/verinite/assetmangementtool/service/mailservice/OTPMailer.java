package com.verinite.assetmangementtool.service.mailservice;

import com.verinite.assetmangementtool.dto.AdminRegistrationDto;
import com.verinite.assetmangementtool.entity.AdminRegistrationEntity;
import com.verinite.assetmangementtool.repository.AdminRegistrationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
public class OTPMailer {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AdminRegistrationRepository adminRegistrationRepository;

    @Autowired
    private ModelMapper modelMapper;

    public void sendOTP(AdminRegistrationDto adminRegistrationDto) throws MessagingException, UnsupportedEncodingException {
        // Generate OTP
        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        adminRegistrationDto.setOtp(otp);
        adminRegistrationDto.setOtpVerify(false);

        // Save admin info
        AdminRegistrationEntity adminRegistrationEntity = modelMapper.map(adminRegistrationDto, AdminRegistrationEntity.class);
        //adminRegistrationRepository.save(adminRegistrationEntity);

        // Send OTP email
        sendOtpMail(adminRegistrationDto.getFirstName(), adminRegistrationDto.getMail(), otp);
    }

    public String sendOtpMail(String name, String email, String otp) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("elangovan.p@verinite.com", "Asset Manager");
        helper.setTo(email);
        helper.setSubject("Your OTP for Asset Management Verification");

        String content = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }" +
                "        .container { background-color: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); max-width: 600px; margin: auto; }" +
                "        .otp-box { font-size: 28px; font-weight: bold; background-color: #03bcff; color: #ffffff; padding: 15px; text-align: center; border-radius: 4px; margin: 20px 0; }" +
                "        .footer { font-size: 14px; color: #888888; text-align: center; margin-top: 20px; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <h2>Hello " + name + ",</h2>" +
                "        <p>Thank you for registering with the Asset Management System. Use the OTP below to verify your email address:</p>" +
                "        <div class=\"otp-box\">" + otp + "</div>" +
                "        <p>Please do not share this OTP with anyone. It is valid for a limited time.</p>" +
                "        <p class=\"footer\">Regards,<br>Asset Management Team - Verinite</p>" +
                "    </div>" +
                "</body>" +
                "</html>";

        helper.setText(content, true); // HTML content
        mailSender.send(message);

        return "OTP sent successfully. Please check your email.";
    }
}
