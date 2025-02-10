package com.ing.hub.credit.loan.config;

/*import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;*/


import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {

        UserDetails admin = User.builder()

                .username("admin")
                .password("{noop}test12345*")
                .roles("ADMIN")
                .build();

        UserDetails userFirst = User.builder()
                .username("memphis")
                .password("{noop}memphis")
                .roles("USER")
                .build();

        UserDetails userSecond = User.builder()
                .username("virgil")
                .password("{noop}virgil")
                .roles("USER")
                .build();


        return new InMemoryUserDetailsManager(admin,userFirst, userSecond);
    }

    @Hidden
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(configurer ->

                configurer
                        .requestMatchers(HttpMethod.PUT,"/loans/*/pay").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET,"/loans/customer/*").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST,"/loans/customer/*").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET,"/installments/loan/*").hasAnyRole("USER", "ADMIN")
        );

        // use HTTP Basic Authentication
        httpSecurity.httpBasic(Customizer.withDefaults());

        // disable Cross Site Request Forgery (CSFR)
        // in general not required for stateles rest apıs that use post,
        // put delete and/or patch

        httpSecurity.csrf(csfr -> csfr.disable());

        httpSecurity.authorizeHttpRequests(
                authorizeRequests -> authorizeRequests.requestMatchers("/swagger-ui/**")
                        .permitAll()
                        .requestMatchers("/api-docs*/**")
                        .permitAll());
        return httpSecurity.build();
    }

/*    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                "/swagger-ui/", "/v3/api-docs/","/swagger-ui.html"
        );
    }*/


}