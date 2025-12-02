package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Configuration;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Security.AuthTokenFilter;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Service.UserDetailsJPAService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration {

    private final UserDetailsJPAService userDetailsJPAService;

    @Autowired
    private AuthTokenFilter authTokenFilter;

    public SpringSecurityConfiguration(UserDetailsJPAService userDetailsService) {
        this.userDetailsJPAService = userDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            
            .csrf(csrf -> csrf.disable())

            
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // 3. API SIN ESTADO (No usar cookies de sesión)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth
                // Rutas públicas
                .requestMatchers("/api/login").permitAll()
                // Rutas protegidas
                .requestMatchers("/usuario/**").hasRole("Administrador")
                .requestMatchers("/usuario/detail/**").hasAnyRole("Administrador", "Cliente", "Usuario")
                .anyRequest().authenticated()
            )

            // 4. ELIMINAR EL LOGIN HTML (Esto arregla el error de Thymeleaf "TemplateInputException")
            .formLogin(form -> form.disable())
            .logout(logout -> logout.disable())

            // 5. Manejo de errores: Devolver 401 JSON en vez de redirigir a HTML
            .exceptionHandling(e -> e.authenticationEntryPoint(
                (request, response, authException) -> {
                    response.setContentType("application/json");
                    response.setStatus(401);
                    response.getWriter().write("{\"error\": \"No autorizado\"}");
                }
            ));

        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Bean para configurar quién puede conectarse (CORS)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite cualquier origen (Frontend en localhost:3000, 4200, etc.)
        configuration.setAllowedOriginPatterns(List.of("*")); 
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}