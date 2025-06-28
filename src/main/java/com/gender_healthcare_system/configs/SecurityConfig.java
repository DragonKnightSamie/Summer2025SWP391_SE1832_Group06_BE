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
            "/api/v1/customer/register",
            "/api/v1/customer/login",
            "/api/v1/admin/login",
            "/api/v1/manager/login",
            "/api/v1/staff/login",
            "/api/v1/consultant/login",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api/v1/momo/payment-transactions/**", //để tạm API của MoMo ở đây để test
            "/api/v1/admin/statistic-reports/**" //để tạm API báo cáo ở đây để test
    };

    // Các API blog công khai (xem, tìm kiếm)
    private static final String[] BLOG_PUBLIC_ENDPOINTS = {
            "/api/v1/blogs/public/**"
    };

    // Các API cần quyền CUSTOMER
    private static final String[] CUSTOMER_AUTHLIST = {
            "/api/v1/customer/profile/**",
            "/api/v1/customer/consultations/**",
            "/api/v1/customer/testing-service-bookings/**",
            "/api/v1/customer/consultant-list/**",
            "/api/v1/customer/payment-transaction/**",
            "/api/v1/customer/testing-services/**",
            "/api/v1/customer/female/**"

    };

    // Các API cần quyền ADMIN
    private static final String[] ADMIN_AUTHLIST = {
            "/api/v1/admin/managers/**"

    };

    // Các API cần quyền MANAGER
    private static final String[] MANAGER_AUTHLIST = {
            "/api/v1/manager/blogs/**",
            "/api/v1/manager/staffs/**",
            "/api/v1/manager/consultants/**",
            "/api/v1/manager/customers/**",
            "/api/v1/manager/testing-service-types/**",
            "/api/v1/manager/testing-service-results/**",
            "/api/v1/manager/testing-services/**",
            "/api/v1/manager/testing-service-forms/**",
            "/api/v1/manager/price-lists/**",
            "/api/v1/manager/image/**",

    };

    // Các API cần quyền STAFF
    private static final String[] STAFF_AUTHLIST = {
            "/api/v1/staff/payments/**",
            "/api/v1/staff/testing-service-bookings/**"
    };

    // Các API cần quyền CONSULTANT
    private static final String[] CONSULTANT_AUTHLIST = {
            "/api/v1/consultant/consultations/**",
            "/api/v1/consultant/profile/**",
            "/api/v1/consultant/certificates/**"
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
