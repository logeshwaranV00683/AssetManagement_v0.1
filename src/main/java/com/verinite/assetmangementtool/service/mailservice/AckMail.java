package com.verinite.assetmangementtool.service.mailservice;

import com.verinite.assetmangementtool.entity.AssetsEntity;
import com.verinite.assetmangementtool.entity.EmployeeEntity;
import com.verinite.assetmangementtool.repository.AssetsRepository;
import com.verinite.assetmangementtool.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class AckMail {
   @Autowired
   private JavaMailSender javaMailSender;

   @Autowired private AssetsRepository assetRepo;
   @Autowired private EmployeeRepository empRepo;

   public ResponseEntity<?> sendAckMail(String empId, List<AssetsEntity> assetsEntityList) throws MessagingException, UnsupportedEncodingException {

      EmployeeEntity empData= empRepo.findByEmpId(empId);
      MimeMessage message = javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message);
      helper.setFrom("bookstore.verinite@gmail.com", "Assets Acknowledgement Notification");
      helper.setTo(empData.getMail());
      String subject = "Assets Acknowledgement";
      helper.setSubject(subject);
      String tableRow="";
      int i=1;
      SimpleDateFormat sf=new SimpleDateFormat("dd-MM-yyyy");
      for (AssetsEntity item:assetsEntityList){
         tableRow=tableRow+  "  <tr class=\"list-item\" style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact;\">\n" +
                 "              <td data-label=\"Type\" class=\"tableitem\" style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact; padding: 10px; border-bottom: 1px solid #cccaca; font-size: 0.85em; text-align: right;\" align=\"right\">"+i+"</td>\n" +
                 "              <td data-label=\"Type\" class=\"tableitem\" style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact; padding: 10px; border-bottom: 1px solid #cccaca; font-size: 0.85em; text-align: left;\" align=\"left\">"+item.getAssetName()+"</td>\n" +
                 "              <td data-label=\"Unit Price\" class=\"tableitem\" style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact; padding: 10px; border-bottom: 1px solid #cccaca; font-size: 0.85em; text-align: right;\" align=\"right\">"+item.getSerialNumber()+"</td>\n" +
                 "              <td data-label=\"Taxable Amount\" class=\"tableitem\" style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact; padding: 10px; border-bottom: 1px solid #cccaca; font-size: 0.85em; text-align: right;\" align=\"right\">"+item.getAssignedBy()+"</td>\n" +
                 "              <td data-label=\"Tax Code\" class=\"tableitem\" style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact; padding: 10px; border-bottom: 1px solid #cccaca; font-size: 0.85em; text-align: right;\" align=\"right\">"+sf.format(item.getAssignedDate())+"</td>\n" +
                 "             \n" +
                 "            </tr> ";
         i++;
      }
//
      String content="<!DOCTYPE html>\n" +
              "<html lang=\"en\" style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact;\">\n" +
              "<head>\n" +
              "    <meta charset=\"UTF-8\">\n" +
              "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
              "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
              "    <title>Acknowledge Email</title>\n" +
              "</head>\n" +
              "<body style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact; background: #00a3e8; font-family: 'Roboto', sans-serif;\">\n" +
              "    <style>\n" +
              "@media screen and (max-width: 767px) {\n" +
              "  h1 {\n" +
              "    font-size: .9em;\n" +
              "  }\n" +
              "\n" +
              "  #invoice {\n" +
              "    width: 100%;\n" +
              "  }\n" +
              "\n" +
              "  #message {\n" +
              "    margin-bottom: 20px;\n" +
              "  }\n" +
              "\n" +
              "  [id*='invoice-'] {\n" +
              "    padding: 20px 10px;\n" +
              "  }\n" +
              "\n" +
              "  .logo {\n" +
              "    width: 140px;\n" +
              "  }\n" +
              "\n" +
              "  .title {\n" +
              "    float: none;\n" +
              "    display: inline-block;\n" +
              "    vertical-align: middle;\n" +
              "    margin-left: 40px;\n" +
              "  }\n" +
              "\n" +
              "  .title p {\n" +
              "    text-align: left;\n" +
              "  }\n" +
              "\n" +
              "  .col-left,\n" +
              ".col-right {\n" +
              "    width: 100%;\n" +
              "  }\n" +
              "\n" +
              "  .table {\n" +
              "    margin-top: 20px;\n" +
              "  }\n" +
              "\n" +
              "  #table {\n" +
              "    white-space: nowrap;\n" +
              "    overflow: auto;\n" +
              "  }\n" +
              "\n" +
              "  td {\n" +
              "    white-space: normal;\n" +
              "  }\n" +
              "\n" +
              "  .cta-group {\n" +
              "    text-align: center;\n" +
              "  }\n" +
              "\n" +
              "  .cta-group.mobile-btn-group {\n" +
              "    display: block;\n" +
              "    margin-bottom: 20px;\n" +
              "  }\n" +
              "\n" +
              "  .table-main {\n" +
              "    border: 0 none;\n" +
              "  }\n" +
              "\n" +
              "  .table-main thead {\n" +
              "    border: none;\n" +
              "    clip: rect(0 0 0 0);\n" +
              "    height: 1px;\n" +
              "    margin: -1px;\n" +
              "    overflow: hidden;\n" +
              "    padding: 0;\n" +
              "    position: absolute;\n" +
              "    width: 1px;\n" +
              "  }\n" +
              "\n" +
              "  .table-main tr {\n" +
              "    border-bottom: 2px solid #eee;\n" +
              "    display: block;\n" +
              "    margin-bottom: 20px;\n" +
              "  }\n" +
              "\n" +
              "  .table-main td {\n" +
              "    font-weight: 700;\n" +
              "    display: block;\n" +
              "    padding-left: 40%;\n" +
              "    max-width: none;\n" +
              "    position: relative;\n" +
              "    border: 1px solid #cccaca;\n" +
              "    text-align: left;\n" +
              "  }\n" +
              "\n" +
              "  .table-main td:before {\n" +
              "    content: attr(data-label);\n" +
              "    position: absolute;\n" +
              "    left: 10px;\n" +
              "    font-weight: normal;\n" +
              "    text-transform: uppercase;\n" +
              "  }\n" +
              "\n" +
              "  .total-row th {\n" +
              "    display: none;\n" +
              "  }\n" +
              "\n" +
              "  .total-row td {\n" +
              "    text-align: left;\n" +
              "  }\n" +
              "\n" +
              "  footer {\n" +
              "    text-align: center;\n" +
              "  }\n" +
              "}\n" +
              "</style>\n" +
              "\n" +
              "\n" +
              "    <div id=\"invoice\" class=\"effect2\" style=\"box-sizing: border-box; -webkit-print-color-adjust: exact; position: relative; margin: 0 auto; width: 700px; background: #FFF;\">\n" +
              "      \n" +
              "      <div id=\"invoice-top\" style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact; padding: 20px; border-bottom: 2px solid #00a63f;\">\n" +
              "        <div class=\"logo\" style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact; display: inline-block; vertical-align: middle; width: 180px; overflow: hidden;\"><img src=\"https://raw.githubusercontent.com/g0bikrishnan/images/main/v.jpg\" alt=\"Logo\" style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact; width: 100%;\"></div>\n" +
              "        <div class=\"title\" style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact; float: right;\">\n" +
              "        \n" +
              "        </div>\n" +
              "      </div>\n" +
              "  \n" +
              "  \n" +
              "      \n" +
              "      <div id=\"invoice-mid\" style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact; padding: 20px; min-height: 110px;\">   \n" +
              "        <div id=\"message\" style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact; margin-bottom: 30px; display: block;\">\n" +
              "          <h2 style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact; font-size: .9em; margin-bottom: 5px; color: #444;\">Hi "+empData.getFirstName()+",</h2>\n" +
              "          <p style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact; font-size: .85em; color: #666; line-height: 1.2em;\"> <span id=\"invoice_num\" style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact;\">The Below listed assets have been handover to you by  IT Department . Kindly provide your confirmation on the assets received</span></p>\n" +
              "        </div>\n" +
              "         <!-- <div class=\"cta-group mobile-btn-group\">\n" +
              "              <a href=\"javascript:void(0);\" class=\"btn-primary\">Approve</a>\n" +
              "              <a href=\"javascript:void(0);\" class=\"btn-default\">Reject</a>\n" +
              "          </div>  -->\n" +
              "  \n" +
              "      </div>\n" +
              "      \n" +
              "      <div id=\"invoice-bot\" style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact; padding: 20px; min-height: 240px;\">\n" +
              "        \n" +
              "        <div id=\"table\" style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact;\">\n" +
              "          <table class=\"table-main\" style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact; width: 100%; border-collapse: collapse;\" width=\"100%\">\n" +
              "            <thead style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact;\">    \n" +
              "                <tr class=\"tabletitle\" style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact;\">\n" +
              "                  <th style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact; font-size: 0.85em; padding: 5px 10px; border-bottom: 2px solid #ddd; text-align: right;\" align=\"right\"># No</th>\n" +
              "                  <th style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact; font-size: 0.85em; padding: 5px 10px; border-bottom: 2px solid #ddd; text-align: left;\" align=\"left\">Asset Name</th>\n" +
              "                  <th style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact; font-size: 0.85em; padding: 5px 10px; border-bottom: 2px solid #ddd; text-align: right;\" align=\"right\">Serial Number</th>\n" +
              "                  <th style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact; font-size: 0.85em; padding: 5px 10px; border-bottom: 2px solid #ddd; text-align: right;\" align=\"right\">Assigned By</th>\n" +
              "                  <th style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact; font-size: 0.85em; padding: 5px 10px; border-bottom: 2px solid #ddd; text-align: right;\" align=\"right\">Assigned Date</th>\n" +
              "            \n" +
              "                </tr>\n" +
              "            </thead>\n" +
              tableRow+
              "         \n" +
              "          </table><br style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact;\"><br style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact;\">\n" +
              "          <div id=\"message\" style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact; margin-bottom: 30px; display: block;\">\n" +
              "            \n" +
              "            <p style=\"margin: 0; box-sizing: border-box;  font-size: 0.85em; color: #666; line-height: 1.2em; text-indent: 10px;\"> Kindly Acknowledge the mail by today EOD</p><br style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact;\">\n" +
              "           <div style=\"margin: 0; box-sizing: border-box;   text-indent: 15px; font-size: 0.85em;\">\n" +
              "            <p style=\"margin: 0; box-sizing: border-box;    color: #666; line-height: 1.2em;\"> Regards ,</p>\n" +
              "            <p style=\"margin: 0; box-sizing: border-box;   color: #666; line-height: 1.2em;\"> IT Team </p>\n" +

              "           </div>\n" +
              "          </div>\n" +
              "        </div><!--End Table-->\n" +
              "        <!-- <div  class=\"cta-group\">\n" +
              "          <a href=\"javascript:void(0);\" class=\"btn-primary\">Approve</a>\n" +
              "          <a href=\"javascript:void(0);\" class=\"btn-default\">Reject</a>\n" +
              "      </div>  -->\n" +
              "        \n" +
              "      </div><!--End InvoiceBot-->\n" +
              "      <footer style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact; border-top: 1px solid #eeeeee; padding: 15px 20px;\">\n" +
              "        <div id=\"legalcopy\" class=\"clearfix\" style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact;\">\n" +
              "          <p class=\"col-right\" style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact; font-size: .75em; color: #666; line-height: 1.2em; float: right;\">Contact us through\n" +
              "              <span class=\"email\" style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact;\"><a href=\"mailto:support@verinite.com\" style=\"margin: 0; box-sizing: border-box; -webkit-print-color-adjust: exact; text-decoration: none; color: #00a63f;\">support.@verinite.com</a></span>\n" +
              "          </p>\n" +
              "        </div>\n" +
              "      </footer>\n" +
              "    </div><!--End Invoice-->\n" +

              "    \n" +
              "    \n" +
              "  \n" +
              "  </body>\n" +
              "</html>";
      helper.setText(content, true);

      javaMailSender.send(message);

      return ResponseEntity.ok(HttpStatus.OK);
   }
}
