package dev.findfirst.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class DefaultEmailService {

  @Autowired
  public JavaMailSender emailSender;

  @Value("${spring.mail.username:findfirst@localmail.com}") String webhost;

  public void sendSimpleEmail(String toAddress, String subject, String message) {
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setTo(toAddress);
    simpleMailMessage.setSubject(subject);
    simpleMailMessage.setText(message);
    simpleMailMessage.setFrom(webhost);
    emailSender.send(simpleMailMessage);
  }
}
