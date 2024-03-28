package ru.otus.hw.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.otus.hw.security.services.CustomUserDetailsService;

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
                .userDetailsService(customUserDetailsService)
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .authorizeHttpRequests((authz) -> authz
                        .antMatchers(HttpMethod.GET, "/books").hasAnyRole("CUSTOMER", "MANAGER")
                        .antMatchers(HttpMethod.GET, "/genres").hasAnyRole("CUSTOMER", "MANAGER")
                        .antMatchers(HttpMethod.GET, "/authors").hasAnyRole("CUSTOMER", "MANAGER")
                        .antMatchers("/books/**", "/genres/**", "/authors/**").hasRole("MANAGER")
                        .anyRequest().denyAll()
                ).build();
    }

}
