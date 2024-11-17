package com.polifono.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.polifono.common.util.RecaptchaUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecaptchaService {

    @Value("${google.recaptcha.secret}") String recaptchaSecret;
    private static final String GOOGLE_RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    private final RestTemplateBuilder restTemplateBuilder;

    public String verifyRecaptcha(String ip, String recaptchaResponse) {
        Map<String, String> body = new HashMap<>();
        body.put("secret", recaptchaSecret);
        body.put("response", recaptchaResponse);
        body.put("remoteip", ip);

        ResponseEntity<Map> recaptchaResponseEntity = restTemplateBuilder.build()
                .postForEntity(GOOGLE_RECAPTCHA_VERIFY_URL + "?secret={secret}&response={response}&remoteip={remoteip}", body, Map.class, body);

        Map<String, Object> responseBody = recaptchaResponseEntity.getBody();
        boolean recaptchaSuccess = (Boolean) responseBody.get("success");

        if (!recaptchaSuccess) {
            List<String> errorCodes = (List) responseBody.get("error-codes");
            return errorCodes.stream().map(RecaptchaUtil.RECAPTCHA_ERROR_CODE::get).collect(Collectors.joining(", "));
        } else {
            return "";
        }
    }
}
