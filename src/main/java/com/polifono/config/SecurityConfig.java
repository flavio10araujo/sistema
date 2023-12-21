package com.polifono.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(requests -> requests
                .requestMatchers("/static/**", "/vendors/**", "/", "/player/create", "/emailconfirmation", "/emailconfirmationresend", "/passwordreset",
                        "/passwordresetresend", "/pagseguronotification", "/contact", "/diploma", "/diploma/**", "/promo/**", "/promos/**", "/loginfb",
                        "/ads.txt", "/privacy").permitAll());

        http.authorizeHttpRequests(requests -> requests
                .requestMatchers("/admin/**").hasAuthority("ADMIN"));

        http.authorizeHttpRequests(requests -> requests
                .requestMatchers("/teacher/**").hasAuthority("TEACHER"));

        http.authorizeHttpRequests(requests -> requests.anyRequest().fullyAuthenticated());

        http.formLogin(form -> form
                .loginPage("/login")
                .failureUrl("/login?error")
                .usernameParameter("email")
                .permitAll()
                .successHandler(authenticationSuccessHandler));

        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll());

        http.csrf(csrf -> csrf
                .ignoringRequestMatchers(request -> "/pagseguronotification".equals(request.getRequestURI())));
        //http.csrf().ignoringAntMatchers("/pagseguronotification");

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
}
