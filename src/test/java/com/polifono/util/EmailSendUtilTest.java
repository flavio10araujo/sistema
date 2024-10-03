package com.polifono.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.polifono.domain.Player;

@ExtendWith(MockitoExtension.class)
public class EmailSendUtilTest {

    public static final String EMAIL_NO_REPLY = "noreply@email.com";
    public static final String EMAIL_COMPANY = "Company Name";
    public static final String EMAIL_URL = "http://localhost:8080";
    public static final String EMAIL_COMPANY_SLOGAN = "Company Slogan";

    @Mock
    private EmailSendUtil emailSendUtil;

    @InjectMocks
    private EmailSendUtilTest emailSendUtilTest;

    @Test
    public void givenPlayer_WhenSendEmailConfirmRegister_ThenSendEmail() {
        emailSendUtil.setEmailNoReply(EMAIL_NO_REPLY);
        emailSendUtil.setEmailCompany(EMAIL_COMPANY);
        emailSendUtil.setEmailUrl(EMAIL_URL);
        emailSendUtil.setEmailCompanySlogan(EMAIL_COMPANY_SLOGAN);

        Player player = Player.builder()
                .name("John Silva")
                .email("john.silva@email.com")
                .emailConfirmed("123456")
                .build();

        String message = "<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"550\" summary=\"\">"
                + "<thead><tr><td align=\"center\"><font color=\"#7EBB3B\" face=\"arial\" size=\"+2\" style=\"text-transform:uppercase\"><b>" + EMAIL_COMPANY + "</b></font><hr size=\"3\" color=\"#7EBB3B\"></td></tr></thead><tbody><tr><td>"
                + "<p><font color=\"#7EBB3B\" face=\"arial\" size=\"+1\"><b>Olá {0},</b></font></p>"
                + "<p><font face=\"arial\" size=\"2\">Você iniciou seu cadastro na plataforma " + EMAIL_COMPANY + " com o e-mail <a href=\"#\">{1}</a>. Para finalizá-lo, precisamos que você valide seu email.</font></p>"
                + "<p><font face=\"arial\" size=\"2\">Para validar:</font></p>"
                + "<p><font face=\"arial\" size=\"2\">- Acesse <a href=\"https://" + EMAIL_URL + "/emailconfirmation\">" + EMAIL_URL + "/emailconfirmation</a></font></p>"
                + "<p><font face=\"arial\" size=\"2\">- Informe o e-mail cadastrado;</font></p>"
                + "<p><font face=\"arial\" size=\"2\">- Informe o seguinte código de ativação: <b>{2}</b></font></p>"
                + "</td></tr><tr><td>"
                + "<font face=\"arial\" size=\"-1\"><br>Atenciosamente,<br>Equipe " + EMAIL_COMPANY + "<br /><br /><strong>" + EMAIL_COMPANY + "</strong> - " + EMAIL_COMPANY_SLOGAN + "</font>"
                + "</td></tr><tr><td align=\"center\">"
                + "<hr size=\"2\" color=\"#EFEFEF\"><font face=\"arial\" size=\"-1\">DÚVIDAS? Acesse <a href=\"https://" + EMAIL_URL + "/#faq\">" + EMAIL_URL + "/#faq</a></font>"
                + "</td></tr><tr><td align=\"center\"><hr size=\"2\" color=\"#EFEFEF\"><font face=\"arial\" size=\"-1\">Este é um e-mail automático disparado pelo sistema. Favor não respondê-lo, pois esta conta não é monitorada.</font>"
                + "</td></tr></tbody></table>";

        //EmailSendUtil.sendEmailConfirmRegister(player);

        // test if EmailSendUtil.MailAsync(EMAIL_NO_REPLY, "Confirme seu cadastro na" + EMAIL_COMPANY, message, player.getEmail()).start(); was called with the correct parameters:

    }
}
