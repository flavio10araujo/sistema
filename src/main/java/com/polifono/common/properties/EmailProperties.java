package com.polifono.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import lombok.Builder;
import lombok.Data;

@Component
@ConfigurationProperties(prefix = "app.email")
@Data
public class EmailProperties {
    private String charset;
    private String hostName;
    private String smtpPort;

    @NestedConfigurationProperty
    private Authentication authentication;

    @Data
    @Builder
    public static class Authentication {
        private String login;
        private String password;

        public Authentication getBaseAuthenticationProperties() {
            return Authentication.builder()
                    .login(login)
                    .password(password)
                    .build();
        }
    }
}
