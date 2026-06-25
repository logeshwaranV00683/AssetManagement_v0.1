package com.verinite.assetmanagementtool.config;

import com.verinite.assetmanagementtool.service.JwtUserDetailsServie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private JwtUserDetailsServie jwtUserDetailsServie;

    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authBuilder) throws Exception {
        authBuilder.userDetailsService(jwtUserDetailsServie).passwordEncoder(passwordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();

    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeRequests(auth -> auth
                        .antMatchers("/login", "/swagger-ui/**", "/v2/api-docs",
                                "/v3/api-docs/**", "/custom-api-docs/**", "/swagger-resources/**", "/configuration/**", "/webjars/**",
                                "/generate/bcrypt", "/reset-password/send-otp", "/reset-password/verify-otp")
                        .permitAll() // Permit all the listed endpoints, including Swagger
                        .anyRequest().authenticated()) // All other endpoints require authentication
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);

        httpSecurity.cors(cors -> cors.configurationSource(configurationSource()));
    }

    private CorsConfigurationSource configurationSource() {

        return new CorsConfigurationSource() {

            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                CorsConfiguration cfg = new CorsConfiguration();

                cfg.setAllowedOrigins(Collections.singletonList("*"));
                cfg.setAllowCredentials(false);

                cfg.setAllowedMethods(Collections.singletonList("*"));

                cfg.setAllowedHeaders(Collections.singletonList("*"));

                cfg.setExposedHeaders(Arrays.asList("Authorization"));

                // Cache CORS settings for 1 hour
                cfg.setMaxAge(3600L);

                return cfg;
            }
        };

    }

}
