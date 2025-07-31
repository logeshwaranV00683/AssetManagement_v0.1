package com.verinite.assetmanagementtool.service.mailservice;

import com.verinite.assetmanagementtool.dto.AdminRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class AdminPromotionMailer {

    @Autowired
    private JavaMailSender mailSender;

    private static String getBody(AdminRegistrationDto adminRegistrationDto, String password) {
        return "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Admin Promotion</title>" +
                "</head>" +
                "<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; margin: 0;\">" +
                "    <div style=\"max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); overflow: hidden;\">" +
                "        <div style=\"background-color: #0072C6; padding: 20px; text-align: center;\">" +
                "          <img src=\"https://raw.githubusercontent.com/g0bikrishnan/images/main/v.jpg\" alt=\"Logo\" style=\"width: 180px;\">\n" +
                "        </div>" +
                "        <div style=\"padding: 30px; color: #333333;\">" +
                "            <h2 style=\"color: #0072C6;\">You're now an Admin!</h2>" +
                "            <p>Hi <strong>" + adminRegistrationDto.getFirstName() + " " + adminRegistrationDto.getLastName() + "</strong>,</p>" +
                "            <p>Congratulations! You’ve been promoted to <strong>Admin</strong> for the Asset Management Tool.</p>" +
                "            <p><strong>Your User ID:</strong> " + adminRegistrationDto.getEmpId() + "</p>" +
                "            <p><strong>Your Temporary Password:</strong> " + password + "</p>" +
                "            <p>Please log in using your credentials and update your password at your earliest convenience.</p>" +
                "            <p style=\"margin-top: 30px;\">Thank you,<br><strong>The Verinite Team</strong></p>" +
                "        </div>" +
                "        <div style=\"background-color: #f0f0f0; padding: 15px; text-align: center; font-size: 12px; color: #888888;\">" +
                "            &copy; Verinite. All rights reserved." +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    public void promoteToAdmin(AdminRegistrationDto adminRegistrationDto, String password) {
        String subject = "You're now an Admin for Asset Management Tool!";
        String body = getBody(adminRegistrationDto, password);
        sendEmail(adminRegistrationDto.getMail(), subject, body);
    }

    public void sendEmail(String to, String subject, String htmlContent) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // 'true' enables HTML support

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // or handle the exception appropriately
        }
    }

}
