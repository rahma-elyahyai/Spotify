package com.endava.mss.serviceImpl;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

import java.io.IOException;

@Service
public class EmailNotificationServiceImpl {

  
    private final JavaMailSender javaMailSender;

    public EmailNotificationServiceImpl(JavaMailSender javaMailSender) {
	
		this.javaMailSender = javaMailSender;
	}

	@Async
    public void sendEmail(String toEmail, String subject, String body) throws MessagingException, IOException, jakarta.mail.MessagingException {
     
        jakarta.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        
     
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true); 

  
        messageHelper.setFrom("melodify2025@gmail.com");
        messageHelper.setTo(toEmail);
        messageHelper.setSubject(subject);
        
        messageHelper.setText(body, true);  
  
        javaMailSender.send(mimeMessage);
        
        System.out.println("Mail sent successfully....");
    }
}
