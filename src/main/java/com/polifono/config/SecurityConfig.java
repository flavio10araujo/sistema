package com.polifono.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*http.authorizeRequests()
                .antMatchers("/vendors/**", "/", "/player/create", "/emailconfirmation", "/emailconfirmationresend", "/passwordreset", "/passwordresetresend", "/pagseguronotification").permitAll() // It's not necessary to be logged in to see this pages.
                .antMatchers("/admin/**").hasAuthority("ADMIN") // It's necessary to have the ADMIN role to see this pages.
                //.anyRequest().fullyAuthenticated()
                .anyRequest().authenticated() // It's necessary to be logged to access the other pages.
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error")
                .usernameParameter("email")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .deleteCookies("remember-me")
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
                .rememberMe();*/
    	
    	
    	http.authorizeRequests().antMatchers("/static/**", "/vendors/**", "/", "/player/create", "/emailconfirmation", "/emailconfirmationresend", "/passwordreset", "/passwordresetresend", "/pagseguronotification", "/contact", "/diploma", "/diploma/**").permitAll(); // It's not necessary to be logged in to see this pages.
    	
    	http.authorizeRequests().antMatchers("/admin/**").hasAuthority("ADMIN"); // It's necessary to have the ADMIN role to see this pages.
    	
    	http.authorizeRequests().antMatchers("/teacher/**").hasAuthority("TEACHER"); // It's necessary to have the TEACHER role to see this pages.
        
    	http.authorizeRequests().anyRequest().fullyAuthenticated(); // It's not possible to access the pages with the remember-me.
    	//http.authorizeRequests().anyRequest().authenticated(); // It's necessary to be logged to access the other pages.
        
    	http.formLogin().loginPage("/login");
    	
    	http.formLogin().failureUrl("/login?error");
    	
    	http.formLogin().usernameParameter("email");
    	
    	http.formLogin().permitAll();
    	
    	http.formLogin().successHandler(authenticationSuccessHandler);
    	
    	http.logout().logoutUrl("/logout");
    	
    	//http.logout().deleteCookies("remember-me");
        
    	http.logout().logoutSuccessUrl("/");
        
    	http.logout().permitAll();
    	
    	//http.rememberMe();
    	
    	//http.csrf().disable();
    	http.csrf().ignoringAntMatchers("/pagseguronotification");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
}