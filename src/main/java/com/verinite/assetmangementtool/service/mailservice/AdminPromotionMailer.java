package com.verinite.assetmangementtool.service.mailservice;

import com.verinite.assetmangementtool.dto.AdminRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class AdminPromotionMailer {

        @Autowired
        private JavaMailSender mailSender;

        public void promoteToAdmin(AdminRegistrationDto adminRegistrationDto, String password) {
            String subject = "You're now an Admin for Asset Management Tool!";
            String body = getBody(adminRegistrationDto,password);
            sendEmail(adminRegistrationDto.getMail(), subject, body);
        }

    private static String getBody(AdminRegistrationDto adminRegistrationDto, String password) {
        return "Hi " + adminRegistrationDto.getFirstName()+ ",\n\n"
                + "You've been promoted to Admin.\n\n"
                + "Your User ID: " + adminRegistrationDto.getEmpId() + "\n\n"
                + "Your Temporary Password: " + password + "\n\n"
                + "Thank you.";
    }
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
