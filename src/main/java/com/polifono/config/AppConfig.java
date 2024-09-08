package com.polifono.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages/messages");
        return messageSource;
    }

    @Value("${email.authentication.login}")
    private String emailLogin;

    @Value("${email.authentication.password}")
    private String emailPassword;

    /*@Value("$(credential.email}")
    private String credentialEmail;

    @Value("${credential.production.token}")
    private String credentialProductionToken;

    @Value("${credential.production.appId}")
    private String credentialProductionAppId;

    @Value("${credential.production.appKey}")
    private String credentialProductionAppKey;*/

    @PostConstruct
    public void init() {
        System.out.println("Email login: " + emailLogin);
        //System.out.println("Email password: " + (emailPassword != null ? "*****" : "Not Set"));
        System.out.println("Email password: " + emailPassword);
        //System.out.println("Credential email: " + credentialEmail);
        //System.out.println("Credential production token: " + credentialProductionToken);
        //System.out.println("Credential production appId: " + credentialProductionAppId);
        //System.out.println("Credential production appKey: " + credentialProductionAppKey);
    }
}
