package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.RestController;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO.IEmail;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.EmailRequestDTO;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final IEmail iEmail; 

    public EmailController(IEmail iEmail) {
        this.iEmail = iEmail;
    }

    @PostMapping("/send")
    public ResponseEntity<Result> sendEmail(@RequestBody EmailRequestDTO request) {

        Result result = iEmail.SendEmail(
                request.getTo(),
                request.getSubject(), 
                request.getBody() 
        );
        
        if (result.correct) {
            return ResponseEntity.ok(result); 
        } else {
            return ResponseEntity.status(result.status).body(result);
        }
    }
}
