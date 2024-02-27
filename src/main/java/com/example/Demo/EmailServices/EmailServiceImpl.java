package com.example.Demo.EmailServices;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
	
	
	
	@Autowired private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String sender;

	@Override
	public String sendHtmlMail(String email, String subject, String body) {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(sender);
			helper.setTo(email);
			helper.setSubject(subject);
			helper.setText(body, true); // Set to true to indicate HTML content
			javaMailSender.send(message);
			return "Mail sent successfully";
		} catch (MessagingException e) {
			return "Error while sending mail";
		}
	}

	@Override
	public String sendSimpleMail(String email, String subject, String body) 
	{	
		try 
		{
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setFrom(sender);
			mailMessage.setTo(email);
			mailMessage.setSubject(subject);
			mailMessage.setText(body);
			
			javaMailSender.send(mailMessage);
			return "Mail Send Successfully";
			
		}
		catch (Exception e) 
		{
			return "Error while Sending Mail";
		}
		
	}

}
