/*
 * package com.verinite.assetmangementtool.controller;
 *
 * import java.io.UnsupportedEncodingException; import java.util.List;
 *
 * import javax.mail.MessagingException;
 *
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.http.ResponseEntity; import
 * org.springframework.web.bind.annotation.CrossOrigin; import
 * org.springframework.web.bind.annotation.PathVariable; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.RequestBody; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.RestController;
 *
 * import com.verinite.assetmangementtool.entity.AssetsEntity; import
 * com.verinite.assetmangementtool.service.serviceImpl.AssignedAssetsServiceImpl; import
 * com.verinite.assetmangementtool.service.mailservice.AckMail;
 *
 * @RestController
 *
 * @RequestMapping("/assetManager/v1/service")
 *
 * @CrossOrigin(origins = "http://localhost:4200") public class MailController {
 *
 * @Autowired private AckMail ackMail;
 *
 * @Autowired private AssignedAssetsServiceImpl assetsServiceImpl;
 *
 * @PostMapping("/send/ack/mail/{empId}") public ResponseEntity<?>
 * sendMailToEmp(@PathVariable String empId, @RequestBody List<AssetsEntity>
 * assetsEntities) throws MessagingException, UnsupportedEncodingException {
 * return ackMail.sendAckMail(empId, assetsEntities); }
 *
 * }
 */