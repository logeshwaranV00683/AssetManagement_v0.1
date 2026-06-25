package com.verinite.assetmanagementtool.service.mailservice;

import com.verinite.assetmanagementtool.entity.AssetsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class NotificationMailer {

    @Autowired
    private JavaMailSender mailSender;

    public ResponseEntity notifyMailer(String mail, List<AssetsEntity> list) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(mail);

        String subject = "Warranty Notification Mail";
        helper.setSubject(subject);

        String tableRow = "";
        if (list.isEmpty()) {
            tableRow = "<tr><td colspan=\"3\" style=\"text-align: center; padding: 10px;\">No assets available for warranty notification</td></tr>";
        } else {
            for (AssetsEntity item : list) {
                tableRow += "  <tr>\n" +
                        "    <td>" + item.getAssetName() + "</td>\n" +
                        "    <td>" + item.getSerialNumber() + "</td>\n" +
                        "    <td>" + item.getWarrantyDate() + "</td>\n" +
                        "  </tr>";
            }
        }

        String content = "<!DOCTYPE html>\n" +
                "<html lang=\"en\" style=\"margin: 0; box-sizing: border-box;\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Warranty Notification</title>\n" +
                "</head>\n" +
                "<body style=\"margin: 0; font-family: 'Roboto', sans-serif; background: #00a3e8;\">\n" +
                "    <div style=\"max-width: 700px; margin: auto; background: #fff; padding: 20px;\">\n" +
                "        <div style=\"border-bottom: 2px solid #00a63f; padding-bottom: 10px;\">\n" +
                "            <img src=\"https://raw.githubusercontent.com/g0bikrishnan/images/main/v.jpg\" alt=\"Logo\" style=\"width: 180px;\">\n" +
                "        </div>\n" +
                "        <div style=\"padding: 20px 0;\">\n" +
                "            <h2 style=\"color: #444; margin-bottom: 5px;\">Hello User!</h2>\n" +
                "            <p style=\"color: #666; font-size: 0.9em;\">The following assets are nearing the end of their warranty period:</p>\n" +
                "        </div>\n" +
                "        <table style=\"width: 100%; border-collapse: collapse; font-size: 0.85em;\">\n" +
                "            <thead>\n" +
                "                <tr style=\"background-color: #f2f2f2;\">\n" +
                "                    <th style=\"padding: 10px; border: 1px solid #cccaca; text-align: left;\">Asset Name</th>\n" +
                "                    <th style=\"padding: 10px; border: 1px solid #cccaca; text-align: left;\">Serial Number</th>\n" +
                "                    <th style=\"padding: 10px; border: 1px solid #cccaca; text-align: left;\">Warranty Date</th>\n" +
                "                </tr>\n" +
                "            </thead>\n" +
                "            <tbody>\n" +
                tableRow +
                "            </tbody>\n" +
                "        </table>\n" +
                "        <div style=\"margin-top: 20px;\">\n" +
                "            <p style=\"color: #666; font-size: 0.85em;\">Please take the necessary actions regarding the warranty renewals.</p>\n" +
                "            <p style=\"color: #666; font-size: 0.85em;\">Regards,<br>IT Team</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        helper.setText(content, true);

        mailSender.send(message);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
