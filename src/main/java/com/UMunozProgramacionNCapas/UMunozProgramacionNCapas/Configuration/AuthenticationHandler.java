package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.core.Authentication;

public class AuthenticationHandler implements AuthenticationSuccessHandler {
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        
        String redirect = request.getContextPath();
        
        if(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Administrador"))){
            redirect = "/usuario";
        } else if(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Cliente"))){
            redirect = "/usuario/?id=";
        } else if(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Usuario"))){
            redirect = "/Usuario/?id=";
        }
        
        response.sendRedirect(redirect);
    }
}

