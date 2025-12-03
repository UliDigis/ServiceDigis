package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Security;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Service.UserDetailsJPAService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsJPAService userDetailsJPAService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        if (path.startsWith("/login") || path.startsWith("/api/login")
                || path.startsWith("/css") || path.startsWith("/js")
                || path.startsWith("/images") || path.startsWith("/error")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = parseJwt(request);

            if (jwt != null) {
                System.out.println("Validando token...");

                if (jwtUtil.validateJwtToken(jwt)) {
                    System.out.println("Token válido");

                    String username = jwtUtil.getUsernameFromToken(jwt);
                    System.out.println("Username del token: " + username);

                    UserDetails userDetails = userDetailsJPAService.loadUserByUsername(username);
                    System.out.println("UserDetails cargado: " + userDetails.getUsername());

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    System.out.println("Autenticación establecida correctamente");
                } else {
                    System.out.println("Token inválido o expirado");
                }
            } else {
                System.out.println("No se encontró token en el header");
            }

        } catch (Exception ex) {
            System.err.println("Error en AuthTokenFilter: " + ex.getMessage());
            ex.printStackTrace();

            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        System.out.println("Authorization header completo: " + headerAuth);

        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            String token = headerAuth.substring(7);
            System.out.println("Token extraído (primeros 20 chars): " +
                    (token.length() > 20 ? token.substring(0, 20) + "..." : token));
            return token;
        }

        return null;
    }
}