package com.verinite.assetmangementtool.config;

import com.verinite.assetmangementtool.service.JwtUserDetailsServie;
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

//	@Override
//	protected void configure(HttpSecurity httpSecurity) throws Exception {
//		httpSecurity.csrf(crfc -> crfc.disable()).authorizeRequests()
//				.antMatchers("/login", "/assetManager/v1/admin/add/admin", "/swagger-ui/**", "/v2/api-docs",
//						"/v3/api-docs/**", "/swagger-resources/**", "/configuration/**", "/webjars/**",
//						"/generate/bcrypt")
//				.permitAll().anyRequest().authenticated().and().exceptionHandling()
//				.authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
//				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//		httpSecurity.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
//		httpSecurity.cors(cors -> cors.configurationSource(configurationSource()));
//	}

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeRequests(auth -> auth
                        .antMatchers(
                                "/login",
                                "/swagger-ui/**",
                                "/v2/api-docs",
                                "/v3/api-docs/**",
                                "/custom-api-docs/**",
                                "/swagger-resources/**",
                                "/configuration/**",
                                "/webjars/**",
                                "/generate/bcrypt",
                                "/reset-password/send-otp",
                                "/reset-password/verify-otp",
                                "/h2-console/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions().disable()); // Enable H2 console frames


        // Add JWT filter
        httpSecurity.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);

        // CORS configuration
        httpSecurity.cors(cors -> cors.configurationSource(configurationSource()));
    }

    //	private CorsConfigurationSource configurationSource() {
//
//		return new CorsConfigurationSource() {
//
//			@Override
//			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
//
//				CorsConfiguration cfg = new CorsConfiguration();
//
//				cfg.setAllowedOrigins(Arrays.asList("http://localhost:3000/", "http://localhost:4200/"));
//				cfg.setAllowedMethods(Collections.singletonList("*"));
//				cfg.setAllowCredentials(true);
//				cfg.setAllowedHeaders(Collections.singletonList("*"));
//				cfg.setExposedHeaders(Arrays.asList("Authorization"));
//				cfg.setMaxAge(3600L);
//				return cfg;
//			}
//		};
//
//	}
    private CorsConfigurationSource configurationSource() {

        return new CorsConfigurationSource() {

            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                CorsConfiguration cfg = new CorsConfiguration();

                // Allow all origins without credentials
                cfg.setAllowedOrigins(Collections.singletonList("*"));
                cfg.setAllowCredentials(false);

                // Allow all HTTP methods (GET, POST, etc.)
                cfg.setAllowedMethods(Collections.singletonList("*"));

                // Allow all headers
                cfg.setAllowedHeaders(Collections.singletonList("*"));

                // Expose Authorization header
                cfg.setExposedHeaders(Arrays.asList("Authorization"));

                // Cache CORS settings for 1 hour
                cfg.setMaxAge(3600L);

                return cfg;
            }
        };

    }

}
