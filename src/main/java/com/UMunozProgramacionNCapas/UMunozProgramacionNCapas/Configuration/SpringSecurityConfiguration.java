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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;

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
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.disable())
            
            
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/login", "/api/login", "/css/**", "/js/**", "/images/**").permitAll()

            
            .requestMatchers("/usuario/**").hasRole("Administrador")
            .requestMatchers("/usuario/detail/**")
                .hasAnyRole("Administrador", "Cliente", "Usuario")

            .anyRequest().authenticated()
        )

        .formLogin(form -> form
            .loginPage("/login")
            .permitAll()
        )

        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login")
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .permitAll()
        );
    
    http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthenticationHandler();
    }
}
