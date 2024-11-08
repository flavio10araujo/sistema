package com.polifono.service.impl;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.polifono.util.EmailUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ContactService {

    private final MessageSource messagesResource;

    public String validateContact(String email, String message, Locale locale) {
        String msg = "";

        if (email == null || email.isEmpty()) {
            msg = msg + messagesResource.getMessage("msg.contact.TheEmailMustBeInformed", null, locale);
        } else if (!EmailUtil.validateEmail(email)) {
            msg = msg + messagesResource.getMessage("msg.contact.TheEmailIsNotValid", null, locale);
        }

        if (message == null || message.isEmpty()) {
            msg = (msg.isEmpty()) ? messagesResource.getMessage("msg.contact.TheMessageMustBeInformed", null, locale)
                    : msg + "<br />" + messagesResource.getMessage("msg.contact.TheMessageMustBeInformed", null, locale);
        }

        return msg;
    }
}
