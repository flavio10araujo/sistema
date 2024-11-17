package com.polifono.service.impl;

import org.apache.commons.mail.MultiPartEmail;
import org.springframework.stereotype.Component;

import com.polifono.common.properties.EmailProperties;
import com.polifono.common.util.HTMLEntitiesUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class EmailSender {

    private final EmailProperties emailProperties;
    private String senderAddress;
    private String subject;
    private String message;
    private String recipientAddress;

    public boolean sendEmail(boolean sync, String senderAddress, String subject, String message, String recipientAddress) {
        this.senderAddress = senderAddress;
        this.subject = subject;
        this.message = message;
        this.recipientAddress = recipientAddress;

        try {
            if (sync) {
                sendEmail();
            } else {
                new MailAsync().start();
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public class MailAsync extends Thread {
        @Override
        public void run() {
            try {
                sendEmail();
            } catch (Exception e) {
                // Do nothing.
            }
        }
    }

    private void sendEmail() throws Exception {
        MultiPartEmail hm = new MultiPartEmail();

        hm.setHostName(emailProperties.getHostName());
        hm.setSmtpPort(Integer.parseInt(emailProperties.getSmtpPort()));
        hm.setAuthentication(emailProperties.getAuthentication().getLogin(), emailProperties.getAuthentication().getPassword());
        hm.setCharset(emailProperties.getCharset());
        hm.setSubject(subject);
        hm.setFrom(senderAddress);
        hm.addTo(recipientAddress);

        message = HTMLEntitiesUtil.decodeHtmlEntities(message);
        message = HTMLEntitiesUtil.encodeHtmlEntities(message);

        hm.addPart(message, org.apache.commons.mail.Email.TEXT_HTML);

        hm.send();
    }
}
