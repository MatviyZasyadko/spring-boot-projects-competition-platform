package com.ukma.competition.platform.main.sender;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MailSenderServiceImpl implements MainSenderService {

   // final JavaMailSender mailSender;
//
   // @Value("${spring.mail.username}")
   // String senderEmail;
//
   // public void sendEmail(String from, String to, String subject, String body) {
   //         SimpleMailMessage message = new SimpleMailMessage();
   //         message.setFrom(senderEmail);
//
   //         message.setTo(senderEmail);
   //         message.setSubject(subject);
   //         message.setText(body);
   //         mailSender.send(message);
   // }

}
