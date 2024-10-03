package com.polifono.util;

import org.apache.commons.mail.MultiPartEmail;
import org.springframework.stereotype.Component;

import com.polifono.common.properties.EmailProperties;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MailAsync extends Thread {

    private final EmailProperties emailProperties;

    private String senderAddress;
    private String recipientAddress;
    private String subject;
    private String message;

    @Override
    public void run() {
        try {
            MultiPartEmail hm = new MultiPartEmail();

            hm.setHostName(emailProperties.getHostName());
            hm.setSmtpPort(Integer.parseInt(emailProperties.getSmtpPort()));
            hm.setAuthentication(emailProperties.getAuthenticationLogin(), emailProperties.getAuthenticationPassword());
            hm.setCharset(emailProperties.getCharset());
            hm.setSubject(subject);
            hm.setFrom(senderAddress);
            hm.addTo(recipientAddress);

            message = HTMLEntitiesUtil.decodeHtmlEntities(message);
            message = HTMLEntitiesUtil.encodeHtmlEntities(message);

            hm.addPart(message, org.apache.commons.mail.Email.TEXT_HTML);

            hm.send();
        } catch (Exception e) {
            throw new RuntimeException("An email was not sent: ", e);
        }
    }

    public void sendEmail(String senderAddress, String recipientAddress, String subject, String message) {
        this.senderAddress = senderAddress;
        this.recipientAddress = recipientAddress;
        this.subject = subject;
        this.message = message;
        this.start();
    }
}
