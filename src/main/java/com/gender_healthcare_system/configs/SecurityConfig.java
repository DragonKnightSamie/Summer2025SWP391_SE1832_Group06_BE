package com.gender_healthcare_system.configs;

import com.gender_healthcare_system.filters.JwtAuthFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    // Các API không cần đăng nhập
    private static final String[] AUTH_WHITELIST = {
            "/customer/register",
            "/customer/login",
            "/admin/login",
            "/manager/login",
            "/staff/login",
            "/consultant/login",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    // Các API blog công khai (xem, tìm kiếm)
    private static final String[] BLOG_PUBLIC_ENDPOINTS = {
            "/api/blogs/**"
    };

    // Các API cần quyền CUSTOMER
    private static final String[] CUSTOMER_AUTHLIST = {
            "/customer/logout",
            "/customer/consultations/**"

    };

    // Các API cần quyền ADMIN
    private static final String[] ADMIN_AUTHLIST = {
            "/admin/logout",
            "/admin/managers/**"

    };

    // Các API cần quyền MANAGER
    private static final String[] MANAGER_AUTHLIST = {
            "/manager/login",
            "/manager/logout",
            "/manager/blogs/**",
            "/manager/staffs/**",
            "/manager/consultants/**",
            "/manager/customers/**",
            "/manager/testing-service-types/**",
            "/manager/testing-service-results/**",
            "/manager/testing-services/**",
            "/manager/testing-service-forms/**",
            "/manager/price-lists/**",
            "/manager/image/**",
            
    };

    // Các API cần quyền STAFF
    private static final String[] STAFF_AUTHLIST = {
            "/staff/payments/**",
            "/staff/logout/",
            "/staff/testing-service-history/**"
    };

    // Các API cần quyền CONSULTANT
    private static final String[] CONSULTANT_AUTHLIST = {
            "/consultant/consultations/**",
            "/consultant/profile/**",
            "/consultant/certificates/**",
            "/consultant/logout/"

    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers(BLOG_PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(ADMIN_AUTHLIST).hasAuthority("ROLE_ADMIN")
                        .requestMatchers(MANAGER_AUTHLIST).hasAuthority("ROLE_MANAGER")
                        .requestMatchers(STAFF_AUTHLIST).hasAuthority("ROLE_STAFF")
                        .requestMatchers(CONSULTANT_AUTHLIST).hasAuthority("ROLE_CONSULTANT")
                        .requestMatchers(CUSTOMER_AUTHLIST).hasAuthority("ROLE_CUSTOMER")
                        //.requestMatchers(CONSULTATION_AUTHLIST).hasAnyAuthority("ROLE_CUSTOMER", "ROLE_CONSULTANT") //Consultation API accessible by both CUSTOMER and CONSULTANT
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider = new
                DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager
            (AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }
}
