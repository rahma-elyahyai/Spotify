package com.endava.mss.serviceImpl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {


	private final JavaMailSender javaMailSender;
	
	public EmailSenderService(JavaMailSender javaMailSender) {
	
		this.javaMailSender = javaMailSender;
	}

	@Async
	public void sendEmail(String toEmail,String subject,String body)
	{
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setFrom("melodify2025@gmail.com");
		message.setTo(toEmail);
		message.setText(body);
		message.setSubject(subject);
		
		javaMailSender.send(message);
	
		System.out.println("Mail sent successfully....");
	}
}
