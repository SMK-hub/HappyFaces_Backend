package com.example.Demo.EmailServices;

public interface EmailService {

	String sendSimpleMail(String email,String subject,String body);
	String sendHtmlMail(String email, String subject, String body);
	
}
