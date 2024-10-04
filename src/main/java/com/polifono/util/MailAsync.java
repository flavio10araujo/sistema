package com.polifono.util;

import org.apache.commons.mail.MultiPartEmail;

import com.polifono.common.properties.EmailProperties;

public class MailAsync extends Thread {

    private final EmailProperties emailProperties;
    private final String senderAddress;
    private final String recipientAddress;
    private final String subject;
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
    
    public MailAsync(EmailProperties emailProperties, String senderAddress, String subject, String message, String recipientAddress) {
        this.emailProperties = emailProperties;
        this.senderAddress = senderAddress;
        this.recipientAddress = recipientAddress;
        this.subject = subject;
        this.message = message;
    }
}
