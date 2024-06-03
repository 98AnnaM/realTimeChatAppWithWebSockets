package com.devexperts.chatapp.config;

import com.devexperts.chatapp.repository.UserRepository;
import com.devexperts.chatapp.service.JwtService;
import com.devexperts.chatapp.user_details.CustomUserDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.Cookie;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

@Configuration
@EnableCaching
public class ApplicationBeanConfiguration {

    @Value("${quotesservice.base.url}")
    private String addressBaseUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder().baseUrl(addressBaseUrl).build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new CustomUserDetailsService(userRepository);
    }

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("users");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserRepository userRepository) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService(userRepository));
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public LogoutHandler logoutHandler() {
        return (request, response, authentication) -> {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                Cookie jwtCookie = Arrays.stream(cookies)
                        .filter(cookie -> cookie.getName().equals("jwtToken"))
                        .findFirst().orElse(null);
                if (jwtCookie != null) {
                    jwtCookie.setMaxAge(0);
                    response.addCookie(jwtCookie);
                }
            }
            SecurityContextHolder.clearContext();
        };
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(JwtService jwtService) {
        return (request, response, authentication) -> {
            String token = jwtService.generateToken(authentication.getName());

            Cookie cookie = new Cookie("jwtToken", token);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            Instant expirationTime = Instant.now().plus(Duration.ofDays(1));
            cookie.setMaxAge(Math.toIntExact(Duration.between(Instant.now(), expirationTime).getSeconds()));
            response.addCookie(cookie);

            response.sendRedirect("/");
        };
    }
}
