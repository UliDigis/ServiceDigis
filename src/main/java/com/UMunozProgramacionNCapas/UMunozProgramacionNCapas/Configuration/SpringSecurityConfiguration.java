package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Configuration;

import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Security.AuthTokenFilter;
import com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Service.UserDetailsJPAService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/login", "/login").permitAll()
                        .requestMatchers("/api/usuario/verify/**").permitAll()
                        .requestMatchers("/usuario/registro").permitAll()

                        .requestMatchers("/usuario/add/**").hasAuthority("ROLE_Administrador")
                        .requestMatchers("/usuario/**").hasAuthority("ROLE_Administrador")
                        .requestMatchers(HttpMethod.GET, "/api/usuario").hasAuthority("ROLE_Administrador")
                        .requestMatchers(HttpMethod.DELETE, "/api/usuario/delete").hasAuthority("ROLE_Administrador")
                        .requestMatchers(HttpMethod.DELETE, "/api/direccion/**").hasAuthority("ROLE_Administrador")

                        .anyRequest().authenticated())

                .formLogin(form -> form.disable())

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(200);
                            response.sendRedirect("/login");
                        })
                        .permitAll())

                .exceptionHandling(e -> e.authenticationEntryPoint(
                        (request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setStatus(401);
                            response.getWriter().write("{\"error\": \"No autorizado (Token inválido o faltante)\"}");
                        }));

        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
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
