package com.verinite.assetmangementtool.service.mailservice;

import com.verinite.assetmangementtool.entity.AssetsEntity;
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
public class NotificationMail {
    @Autowired
    private JavaMailSender mailSender;
    public ResponseEntity nofiyMailer(String mail, List<AssetsEntity> list) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("bookstore.verinite@gmail.com", "Asstemanager Notification");
        helper.setTo(mail);

        String subject = "Warranty Notification";

        helper.setSubject(subject);

        String tableRow="";
        for (AssetsEntity item:list){
           tableRow=tableRow+  "  <tr>\n" +
                   "    <td>"+item.getAssetName()+"</td>\n" +
                   "    <td>"+item.getSerialNumber()+"</td>\n" +
                   "    <td>"+item.getWarrantyDate()+"</td>\n" +
                   "  </tr>";


        }
        String content="<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "    body{\n" +
                "        margin: 20px;\n" +
                "    }\n" +
                "table {\n" +
                "  font-family: arial, sans-serif;\n" +
                "  border-collapse: collapse;\n" +
                "  width: 100%;\n" +
                "}\n" +
                "\n" +
                "td, th {\n" +
                "  border: 1px solid #dddddd;\n" +
                "  text-align: left;\n" +
                "  padding: 8px;\n" +
                "}\n" +
                "\n" +
                "tr:nth-child(even) {\n" +
                "  background-color: #dddddd;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h2> <b> Hello User !. </b></h2><br>\n" +
                " <h3>The Following Assets are in end of warranty Period   </h3>\n" +
                "\n" +
                "<table>\n" +
                "  <tr>\n" +
                "    <th>Name</th>\n" +
                "    <th>Serial Number</th>\n" +
                "    <th>Warranty Date</th>\n" +
                "  </tr>\n" +
              tableRow+
                "  \n" +
                "</table>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
        helper.setText(content, true);

        mailSender.send(message);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
