package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.DAO;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.JPA.Result;

public interface IEmail {
    
    Result SendEmail(String toEmail, String subject, String body);
    
}
