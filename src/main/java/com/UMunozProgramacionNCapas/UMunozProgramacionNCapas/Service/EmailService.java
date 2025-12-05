package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Service;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Autowired
    private JavaMailSender javaMailSender;

    public boolean sendEmail(String toEmail, String subject, String body) {
        Result result = new Result();
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            
            javaMailSender.send(message);
            return true;

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
        }
        return true;
    }

}
