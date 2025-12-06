package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Service;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Autowired
    private JavaMailSender javaMailSender;

    public boolean sendHtmlEmail(String toEmail, String subject, String htmlBody) {
        Result result = new Result(); 
        
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            
            helper.setText(htmlBody, true); 
            
            javaMailSender.send(message);
            
            return true;

        } catch (MessagingException | RuntimeException ex) {
            result.correct = false;
            result.errorMessage = ex.getMessage();
            System.err.println("Error al enviar correo HTML: " + ex.getMessage());
            
            return false; 
        }
    }
}
