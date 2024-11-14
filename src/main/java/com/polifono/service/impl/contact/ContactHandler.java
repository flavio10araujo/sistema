package com.polifono.service.impl.contact;

import static com.polifono.common.constant.TemplateConstants.URL_INDEX;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.polifono.model.entity.Player;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ContactHandler {

    private final MessageSource messagesResource;

    public String handleSuccess(Model model) {
        model.addAttribute("message", "success");
        model.addAttribute("player", new Player());
        model.addAttribute("playerResend", new Player());
        return URL_INDEX;
    }

    public String handleCaptchaError(Model model, Locale locale) {
        return handleError(model, messagesResource.getMessage("msg.captcha.PleaseCheckTheFieldIAmNotARobot", null, locale));
    }

    public String handleError(Model model, String msg) {
        model.addAttribute("message", "error");
        model.addAttribute("player", new Player());
        model.addAttribute("playerResend", new Player());
        model.addAttribute("messageContent", msg);
        return URL_INDEX;
    }
}
