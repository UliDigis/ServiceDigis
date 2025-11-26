package com.UMunozProgramacionNCapas.UMunozProgramacionNCapas.Configuration;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.web.cors.CorsConfiguration;

import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import org.springframework.web.filter.CorsFilter;

@Configuration

public class CorsFilterConfiguration {

    @Bean

    public CorsFilter corsFilter() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfig = new CorsConfiguration();

        corsConfig.addAllowedOriginPattern("*");

        corsConfig.addAllowedMethod(HttpMethod.GET);

        corsConfig.addAllowedMethod(HttpMethod.POST);

        corsConfig.addAllowedMethod(HttpMethod.PUT);

        corsConfig.addAllowedMethod(HttpMethod.PATCH);

        corsConfig.addAllowedMethod(HttpMethod.DELETE);

        corsConfig.addAllowedHeader("*");

        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsFilter(source);

    }

}
