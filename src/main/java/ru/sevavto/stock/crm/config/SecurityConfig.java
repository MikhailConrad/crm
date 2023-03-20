package ru.sevavto.stock.crm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.sevavto.stock.crm.security.JwtFilter;
import ru.sevavto.stock.crm.service.CrmUserService;

@Configuration
@EnableWebSecurity(debug=true)
@EnableMethodSecurity
public class SecurityConfig {
    private final CrmUserService crmUserService;
    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(CrmUserService crmUserService, JwtFilter jwtFilter) {
        this.crmUserService = crmUserService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/auth/**").permitAll()
                //.requestMatchers("/order/**").hasAnyAuthority("MANAGER", "ANALYTIC", "ADMIN")
                //.requestMatchers("/organization/**").hasAnyAuthority("ADMIN")
                //.requestMatchers("/organization_manager/**").hasAnyAuthority("MANAGER", "ADMIN")
                //.requestMatchers("/product/**").hasAnyAuthority("MANAGER", "ANALYTIC", "ADMIN")
                .anyRequest().authenticated()
            .and().authenticationProvider(daoAuthenticationProvider())
                  .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                  .csrf().disable()
                  .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //нужна ли сессия в API?

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(crmUserService);
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
