package com.polifono.service.contact;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.polifono.model.entity.Player;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ContactModelPreparer {

    private final MessageSource messagesResource;

    public void prepareModelForContactSuccess(Model model) {
        model.addAttribute("message", "success");
        model.addAttribute("player", new Player());
        model.addAttribute("playerResend", new Player());
    }

    public void prepareModelForCaptchaError(Model model, Locale locale) {
        prepareModelForContactError(model, messagesResource.getMessage("msg.captcha.PleaseCheckTheFieldIAmNotARobot", null, locale));
    }

    public void prepareModelForContactError(Model model, String msg) {
        model.addAttribute("message", "error");
        model.addAttribute("player", new Player());
        model.addAttribute("playerResend", new Player());
        model.addAttribute("messageContent", msg);
    }
}
