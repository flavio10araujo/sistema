package com.polifono.util;

import org.apache.commons.mail.MultiPartEmail;
import org.springframework.stereotype.Component;

import com.polifono.common.properties.EmailProperties;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class MailSync {

    private final EmailProperties emailProperties;

    public boolean sendEmail(String senderAddress, String recipientAddress, String subject, String message) {
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

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
