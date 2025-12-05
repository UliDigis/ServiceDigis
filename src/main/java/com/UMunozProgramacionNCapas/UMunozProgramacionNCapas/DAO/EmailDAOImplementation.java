package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Service.EmailService;
import org.springframework.stereotype.Repository;

@Repository
public class EmailDAOImplementation implements IEmail {

    private final EmailService emailService;

    public EmailDAOImplementation(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public Result SendEmail(String toEmail, String subject, String body) {
        Result result = new Result();

        boolean enviado = emailService.sendEmail(toEmail, subject, body);

        result.correct = enviado;
        result.Object = enviado; 

        if (!enviado) {
            result.errorMessage = "No se pudo mandar el correo";
        }

        return result;
    }

}
