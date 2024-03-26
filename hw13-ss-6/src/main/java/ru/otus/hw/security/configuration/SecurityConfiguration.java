package ru.otus.hw.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.otus.hw.security.services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   CustomUserDetailsService customUserDetailsService) throws Exception {
        return http
                .csrf(Customizer.withDefaults())
                .userDetailsService(customUserDetailsService)
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(HttpMethod.GET, "/books").hasAnyRole("CUSTOMER", "MANAGER")
                        .requestMatchers(HttpMethod.GET, "/genres").hasAnyRole("CUSTOMER", "MANAGER")
                        .requestMatchers(HttpMethod.GET, "/authors").hasAnyRole("CUSTOMER", "MANAGER")
                        .requestMatchers("/books/**", "/genres/**", "/authors/**").hasRole("MANAGER")
                        .anyRequest().denyAll()
                ).build();
    }

}
