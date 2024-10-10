package com.polifono.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "app.email")
@Data
public class EmailProperties {
    private String charset;
    private String hostName;
    private String smtpPort;
    private String authenticationLogin;
    private String authenticationPassword;
}
