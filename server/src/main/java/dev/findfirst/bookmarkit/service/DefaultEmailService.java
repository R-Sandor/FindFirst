package dev.findfirst.bookmarkit.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class DefaultEmailService  {

 @Autowired
 public JavaMailSender emailSender;

 public void sendSimpleEmail(String toAddress, String subject, String message) {

  SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
  simpleMailMessage.setTo(toAddress);
  simpleMailMessage.setSubject(subject);
  simpleMailMessage.setText(message);
  emailSender.send(simpleMailMessage);
 }

 public void sendEmailWithAttachment(String toAddress, String subject, String message, String attachment) throws Exception  {
  MimeMessage mimeMessage = emailSender.createMimeMessage();
  MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
  messageHelper.setTo(toAddress);
  messageHelper.setSubject(subject);
  messageHelper.setText(message);
  FileSystemResource file = new FileSystemResource(ResourceUtils.getFile(attachment));
  messageHelper.addAttachment("Purchase Order", file);
  emailSender.send(mimeMessage);
 }
}
