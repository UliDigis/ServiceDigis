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

    public boolean sendVerificationEmail(int idUsuario, String toEmail, String baseUrl) {
    String verifyUrl = baseUrl + "/api/usuario/verify/" + idUsuario;

    String html = """
        <div style="font-family: Arial, sans-serif; max-width: 480px; margin: 0 auto; padding: 16px; border: 1px solid #e5e7eb; border-radius: 8px;">
          <h2 style="color: #111827; margin-bottom: 8px;">Confirma tu cuenta</h2>
          <p style="color: #374151; line-height: 1.6; margin-bottom: 16px;">
            Gracias por registrarte. Haz clic en el botón para verificar tu cuenta.
          </p>
          <a href="%s" style="display: inline-block; padding: 12px 20px; background: #2563eb; color: #fff; text-decoration: none; border-radius: 6px;">Verificar cuenta</a>
          <p style="color: #6b7280; font-size: 12px; margin-top: 16px;">
            Si el botón no funciona, copia y pega este enlace en tu navegador:<br>%s
          </p>
        </div>
        """.formatted(verifyUrl, verifyUrl);

    return sendHtmlEmail(toEmail, "Confirma tu cuenta", html);
}


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
