package com.polifono.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.polifono.domain.ClassPlayer;
import com.polifono.domain.Player;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailSendUtil {

    @Value("${app.general.name}")
    private String emailCompany;
    @Value("${app.general.slogan}")
    private String emailCompanySlogan;
    @Value("${app.general.url}")
    private String emailUrl;
    @Value("${app.email.accounts.general.address}")
    private String emailGeneral;
    @Value("${app.email.accounts.noReply.address}")
    private String emailNoReply;
    @Value("${app.email.accounts.general.to}")
    private String emailGeneralTo;

    private final SendEmailService sendEmailService;

    private void sendHtmlMail(boolean sync, int messageType, String to, String[] args) {
        String from = "", subject = "", message = "";

        // TODO - i18n
        if (messageType == 1) {
            from = emailNoReply;
            subject = "Confirme seu cadastro na " + emailCompany;
            message = "<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"550\" summary=\"\">"
                    + "<thead><tr><td align=\"center\"><font color=\"#7EBB3B\" face=\"arial\" size=\"+2\" style=\"text-transform:uppercase\"><b>" + emailCompany + "</b></font><hr size=\"3\" color=\"#7EBB3B\"></td></tr></thead><tbody><tr><td>"
                    + "<p><font color=\"#7EBB3B\" face=\"arial\" size=\"+1\"><b>Olá {0},</b></font></p>"
                    + "<p><font face=\"arial\" size=\"2\">Você iniciou seu cadastro na plataforma " + emailCompany + " com o e-mail <a href=\"#\">{1}</a>. Para finalizá-lo, precisamos que você valide seu email.</font></p>"
                    + "<p><font face=\"arial\" size=\"2\">Para validar:</font></p>"
                    + "<p><font face=\"arial\" size=\"2\">- Acesse <a href=\"https://" + emailUrl + "/emailconfirmation\">" + emailUrl + "/emailconfirmation</a></font></p>"
                    + "<p><font face=\"arial\" size=\"2\">- Informe o e-mail cadastrado;</font></p>"
                    + "<p><font face=\"arial\" size=\"2\">- Informe o seguinte código de ativação: <b>{2}</b></font></p>"
                    + "</td></tr><tr><td>"
                    + "<font face=\"arial\" size=\"-1\"><br>Atenciosamente,<br>Equipe " + emailCompany + "<br /><br /><strong>" + emailCompany + "</strong> - " + emailCompanySlogan + "</font>"
                    + "</td></tr><tr><td align=\"center\">"
                    + "<hr size=\"2\" color=\"#EFEFEF\"><font face=\"arial\" size=\"-1\">DÚVIDAS? Acesse <a href=\"https://" + emailUrl + "/#faq\">" + emailUrl + "/#faq</a></font>"
                    + "</td></tr><tr><td align=\"center\"><hr size=\"2\" color=\"#EFEFEF\"><font face=\"arial\" size=\"-1\">Este é um e-mail automático disparado pelo sistema. Favor não respondê-lo, pois esta conta não é monitorada.</font>"
                    + "</td></tr></tbody></table>";

            message = replaceParamsMessage(message, args);
        } else if (messageType == 2) {
            from = emailNoReply;
            subject = "Sua solicitação de alteração de senha na " + emailCompany;
            message = "<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"550\" summary=\"\">"
                    + "<thead><tr><td align=\"center\"><font color=\"#7EBB3B\" face=\"arial\" size=\"+2\" style=\"text-transform:uppercase\"><b>" + emailCompany + "</b></font><hr size=\"3\" color=\"#7EBB3B\"></td></tr></thead><tbody><tr><td>"
                    + "<p><font color=\"#7EBB3B\" face=\"arial\" size=\"+1\"><b>Olá {0},</b></font></p>"
                    + "<p><font face=\"arial\" size=\"2\">Você (ou alguém) solicitou a alteração da senha na plataforma " + emailCompany + " para o login <a href=\"#\">{1}</a>.</font></p>"
                    + "<p><font face=\"arial\" size=\"2\">Para dar continuidade na alteração da senha:</font></p>"
                    + "<p><font face=\"arial\" size=\"2\">- Acesse <a href=\"https://" + emailUrl + "\">" + emailUrl + "</a></font></p>"
                    + "<p><font face=\"arial\" size=\"2\">- Clique no botão [ENTRAR];</font></p>"
                    + "<p><font face=\"arial\" size=\"2\">- Clique no link [Esqueci minha senha];</font></p>"
                    + "<p><font face=\"arial\" size=\"2\">- Informe o login cadastrado;</font></p>"
                    + "<p><font face=\"arial\" size=\"2\">- Informe o seguinte código de confirmação da alteração da senha: <b>{2}</b></font></p>"
                    + "<p><font face=\"arial\" size=\"2\">- Informe a nova senha desejada.</font></p>"
                    + "<p><font face=\"arial\" size=\"2\"><br /><br />Caso a alteração de senha não seja mais necessária, apenas ignore este e-mail.</font></p>"
                    + "</td></tr><tr><td>"
                    + "<font face=\"arial\" size=\"-1\"><br />Atenciosamente,<br />Equipe " + emailCompany + "<br /><br /><strong>" + emailCompany + "</strong> - " + emailCompanySlogan + "</font>"
                    + "</td></tr><tr><td align=\"center\">"
                    + "<hr size=\"2\" color=\"#EFEFEF\"><font face=\"arial\" size=\"-1\">DÚVIDAS? Acesse <a href=\"https://" + emailUrl + "/#faq\">" + emailUrl + "/#faq</a></font>"
                    + "</td></tr><tr><td align=\"center\"><hr size=\"2\" color=\"#EFEFEF\"><font face=\"arial\" size=\"-1\">Este é um e-mail automático disparado pelo sistema. Favor não respondê-lo, pois esta conta não é monitorada.</font>"
                    + "</td></tr></tbody></table>";

            message = replaceParamsMessage(message, args);
        } else if (messageType == 3) {
            from = emailGeneral;
            subject = "Sua compra de créditos foi confirmada na " + emailCompany;
            message = "<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"550\" summary=\"\">"
                    + "<thead><tr><td align=\"center\"><font color=\"#7EBB3B\" face=\"arial\" size=\"+2\" style=\"text-transform:uppercase\"><b>" + emailCompany + "</b></font><hr size=\"3\" color=\"#7EBB3B\"></td></tr></thead><tbody><tr><td>"
                    + "<p><font color=\"#7EBB3B\" face=\"arial\" size=\"+1\"><b>Olá {0},</b></font></p>"
                    + "<p><font face=\"arial\" size=\"2\">A sua compra foi confirmada e os créditos adquiridos já estão disponíveis em sua conta.</font></p>"
                    + "<p><font face=\"arial\" size=\"2\"><br />O total de créditos adquiridos foi de: <b>{1}</b></font></p>"
                    + "<p><font face=\"arial\" size=\"2\">Obs.: caso esteja logado no sistema, é necessário deslogar e logar novamente para que os novos créditos sejam mostrados.</b></font></p>"
                    + "<p><font face=\"arial\" size=\"2\"><br /><br />Não perca tempo e comece a estudar agora mesmo.</font></p>"
                    + "</td></tr><tr><td>"
                    + "<font face=\"arial\" size=\"-1\"><br />Atenciosamente,<br />Equipe " + emailCompany + "<br /><br /><strong>" + emailCompany + "</strong> - " + emailCompanySlogan + "</font>"
                    + "</td></tr><tr><td align=\"center\">"
                    + "<hr size=\"2\" color=\"#EFEFEF\"><font face=\"arial\" size=\"-1\">DÚVIDAS? Acesse <a href=\"https://" + emailUrl + "/#faq\">" + emailUrl + "/#faq</a></font>"
                    + "</td></tr><tr><td align=\"center\"><hr size=\"2\" color=\"#EFEFEF\">"
                    + "</td></tr></tbody></table>";

            message = replaceParamsMessage(message, args);
        } else if (messageType == 4) {
            from = emailGeneral;
            subject = "Você foi convidado para uma sala de aula na " + emailCompany;
            message = "<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"550\" summary=\"\">"
                    + "<thead><tr><td align=\"center\"><font color=\"#7EBB3B\" face=\"arial\" size=\"+2\" style=\"text-transform:uppercase\"><b>" + emailCompany + "</b></font><hr size=\"3\" color=\"#7EBB3B\"></td></tr></thead><tbody><tr><td>"
                    + "<p><font color=\"#7EBB3B\" face=\"arial\" size=\"+1\"><b>Olá {0},</b></font></p>"
                    + "<p><font face=\"arial\" size=\"2\">O professor {1} te adicionou na sala de aula {2}.</font></p>"
                    + "<p><font face=\"arial\" size=\"2\"><br />Caso você queira participar dessa sala de aula, acesse o seguinte link para confirmar sua participação:</font></p>"
                    + "<p><font face=\"arial\" size=\"2\"><br /><br /><a href=\"https://" + emailUrl + "/classinvitation\">" + emailUrl + "/classinvitation</a></font></p>"
                    + "</td></tr><tr><td>"
                    + "<font face=\"arial\" size=\"-1\"><br />Atenciosamente,<br />Equipe " + emailCompany + "<br /><br /><strong>" + emailCompany + "</strong> - " + emailCompanySlogan + "</font>"
                    + "</td></tr><tr><td align=\"center\">"
                    + "<hr size=\"2\" color=\"#EFEFEF\"><font face=\"arial\" size=\"-1\">DÚVIDAS? Acesse <a href=\"https://" + emailUrl + "/#faq\">" + emailUrl + "/#faq</a></font>"
                    + "</td></tr><tr><td align=\"center\"><hr size=\"2\" color=\"#EFEFEF\">"
                    + "</td></tr></tbody></table>";

            message = replaceParamsMessage(message, args);
        } else if (messageType == 5) {
            from = emailGeneral;
            to = emailGeneralTo;
            subject = "Novo contato: " + args[0];
            message = "<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"550\" summary=\"\">"
                    + "<thead><tr><td align=\"center\"><font color=\"#7EBB3B\" face=\"arial\" size=\"+2\" style=\"text-transform:uppercase\"><b>" + emailCompany + "</b></font><hr size=\"3\" color=\"#7EBB3B\"></td></tr></thead><tbody><tr><td>"
                    + "<p><font color=\"#7EBB3B\" face=\"arial\" size=\"+1\"><b>A seguinte mensagem foi enviada por {0}:</b></font></p>"
                    + "<p><font face=\"arial\" size=\"2\">Mensagem: {1} </font></p>"
                    + "</td></tr><tr><td>"
                    + "<font face=\"arial\" size=\"-1\"><br />Atenciosamente,<br />Equipe " + emailCompany + "<br /><br /><strong>" + emailCompany + "</strong> - " + emailCompanySlogan + "</font>"
                    + "</td></tr><tr><td align=\"center\">"
                    + "<hr size=\"2\" color=\"#EFEFEF\"><font face=\"arial\" size=\"-1\">DÚVIDAS? Acesse <a href=\"https://" + emailUrl + "/#faq\">" + emailUrl + "/#faq</a></font>"
                    + "</td></tr><tr><td align=\"center\"><hr size=\"2\" color=\"#EFEFEF\">"
                    + "</td></tr></tbody></table>";

            message = replaceParamsMessage(message, args);
        }

        sendEmailService.sendEmail(sync, from, subject, message, to);
    }

    private void sendMessageCommunication(boolean sync, int messageType, String to, String[] args) {
        String from = "", subject = "", message = "";

        if (messageType == 4) {
            from = emailGeneral;
            subject = "Sentimos sua falta na " + emailCompany + "... Acesse sua conta para continuar a aprender música!";

            message = "<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"550\" summary=\"\">";

            message = message + "<thead><tr><td align=\"center\"><font color=\"#7EBB3B\" face=\"arial\" size=\"+2\" style=\"text-transform:uppercase\"><b>" + emailCompany + "</b></font><hr size=\"3\" color=\"#7EBB3B\"></td></tr></thead>";

            message = message + "<tbody><tr><td>"
                    + "<p><font color=\"#7EBB3B\" face=\"arial\" size=\"+1\"><b>Olá {0},</b></font></p>"
                    + "<p><font face=\"arial\" size=\"2\">Sentimos sua falta! Já faz tempo que você não acessa a " + emailCompany + "! "
                    + "<p><font face=\"arial\" size=\"2\">Estamos sempre melhorando nosso sistema e já temos cursos de trompete, violão, saxofone, flauta doce e teoria musical.</font></p>"
                    + "<p><font face=\"arial\" size=\"2\">Para continuar a estudar música com a gente:</font></p>"
                    + "<p><font face=\"arial\" size=\"2\">- Acesse <a href=\"https://" + emailUrl + "\">" + emailUrl + "</a></font></p>";

            if (args[1] != null) {
                message = message + "<p><font face=\"arial\" size=\"2\">- Clique no botão [Facebook] que aparecerá na tela;</font></p>";
            } else {
                message = message + "<p><font face=\"arial\" size=\"2\">- Clique no botão [ENTRAR] (caso esteja acessando com celular, antes é preciso clicar no botão para mostrar as opções);</font></p>"
                        + "<p><font face=\"arial\" size=\"2\">- Informe o seu email e sua senha e clique no botão [ENTRAR];</font></p>"
                        + "<p><font face=\"arial\" size=\"2\">- Caso tenha esquecido a sua senha, faça:</font></p>"
                        + "<p><font face=\"arial\" size=\"2\">- - Clique no link [Esqueci minha senha];</font></p>"
                        + "<p><font face=\"arial\" size=\"2\">- - Informe o seu email;</font></p>"
                        + "<p><font face=\"arial\" size=\"2\">- - Clique no botão [ENVIAR] e você receberá um email com as instruções para a troca da senha.</font></p>";
            }

            message = message + "<p><font face=\"arial\" size=\"2\"><br />Após acessar o sistema, escolha o curso de música que você deseja fazer e bons estudos!.</font></p></td></tr>";

            message = message + "<tr><td><font face=\"arial\" size=\"-1\"><br />Aguardamos você!<br />Equipe " + emailCompany + "<br /><br /><strong>" + emailCompany + "</strong> - " + emailCompanySlogan + "</font></td></tr>";

            message = message + "<tr><td align=\"center\">"
                    + "<hr size=\"2\" color=\"#EFEFEF\"><font face=\"arial\" size=\"-1\">DÚVIDAS? Acesse <a href=\"https://" + emailUrl + "/#faq\">" + emailUrl + "/#faq</a></font>"
                    + "</td></tr><tr><td align=\"center\"><hr size=\"2\" color=\"#EFEFEF\"><font face=\"arial\" size=\"-1\"></font></td></tr>";

            message = message + "<tr><td><p><font color=\"#7EBB3B\" face=\"arial\" size=\"+1\"><b>Promoções e novidades:</b></font></p>"
                    + "<p><font face=\"arial\" size=\"2\">Siga-nos nas redes sociais para aproveitar as promoções e poder fazer aulas de música de graça!</font></p>"
                    + "<p><font face=\"arial\" size=\"2\"><a href=\"https://www.facebook.com/polifonooficial/\">Facebook</a> - <a href=\"https://www.youtube.com/c/PolifonoOficial\">Youtube</a> - <a href=\"https://www.instagram.com/polifono_music/\">Instagram</a></font></p>"
                    + "</td></tr>";

            message = message + "</tbody></table>";

            message = replaceParamsMessage(message, args);
        } else if (messageType == 5) {
            from = emailGeneral;
            subject = "Sentimos sua falta na " + emailCompany + "... Acesse sua conta para continuar a aprender música!";

            message = "<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"550\" summary=\"\">";

            message = message + "<thead><tr><td align=\"center\"><font color=\"#7EBB3B\" face=\"arial\" size=\"+2\" style=\"text-transform:uppercase\"><b>" + emailCompany + "</b></font><hr size=\"3\" color=\"#7EBB3B\"></td></tr></thead>";

            message = message + "<tbody><tr><td>"
                    + "<p><font color=\"#7EBB3B\" face=\"arial\" size=\"+1\"><b>Olá {0},</b></font></p>"
                    + "<p><font face=\"arial\" size=\"2\">Sentimos sua falta! Já faz tempo que você não acessa a " + emailCompany + "! "
                    + "<p><font face=\"arial\" size=\"2\">Estamos sempre melhorando nosso sistema, criando novas funcionalidades e adicionando novas aulas.</font></p>";

            // Credits > 0
            if (Integer.parseInt(args[2]) > 0) {
                message = message + "<p><font face=\"arial\" size=\"2\">Você ainda tem créditos em sua conta da sua última compra!</font></p>";
            }

            message = message + "<p><font face=\"arial\" size=\"2\">Mini relatório do aluno:</font></p>"
                    + "<p><font face=\"arial\" size=\"2\">- Créditos em conta: {2}</font></p>"
                    + "<p><font face=\"arial\" size=\"2\">- Pontuação total: {3}</font></p>"
                    + "<p><font face=\"arial\" size=\"2\">- Nível musical: {4}</font></p>";

            message = message + "<p><font face=\"arial\" size=\"2\"><br />Acesse <a href=\"https://" + emailUrl + "\">" + emailUrl + "</a> e continue estudando para subir de nível!</font></p></td></tr>";

            message = message + "<tr><td><font face=\"arial\" size=\"-1\"><br />Aguardamos você!<br />Equipe " + emailCompany + "<br /><br /><strong>" + emailCompany + "</strong> - " + emailCompanySlogan + "</font></td></tr>";

            message = message + "<tr><td align=\"center\">"
                    + "<hr size=\"2\" color=\"#EFEFEF\"><font face=\"arial\" size=\"-1\">DÚVIDAS? Acesse <a href=\"https://" + emailUrl + "/#faq\">" + emailUrl + "/#faq</a></font>"
                    + "</td></tr><tr><td align=\"center\"><hr size=\"2\" color=\"#EFEFEF\"><font face=\"arial\" size=\"-1\"></font></td></tr>";

            message = message + "<tr><td><p><font color=\"#7EBB3B\" face=\"arial\" size=\"+1\"><b>Promoções e novidades:</b></font></p>"
                    + "<p><font face=\"arial\" size=\"2\">Siga-nos nas redes sociais para aproveitar as promoções e ganhar créditos!</font></p>"
                    + "<p><font face=\"arial\" size=\"2\"><a href=\"https://www.facebook.com/polifonooficial/\">Facebook</a> - <a href=\"https://www.youtube.com/c/PolifonoOficial\">Youtube</a> - <a href=\"https://www.instagram.com/polifono_music/\">Instagram</a></font></p>"
                    + "</td></tr>";

            message = message + "</tbody></table>";

            message = replaceParamsMessage(message, args);
        }

        sendEmailService.sendEmail(sync, from, subject, message, to);
    }

    /**
     * This method get an email and replaces the strings into the email by the args strings.<br>
     * Eg:<br>
     * {0} will be replaced by the first argument into the array<br>
     * {1} will be replaced by the second argument into the array
     *
     * @param message the message
     * @param args    the string to replace into the email string.
     * @return The new string email with the correct messages.
     */
    private String replaceParamsMessage(String message, String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null) {
                message = message.replace("{" + i + "}", args[i]);
            }
        }

        return message;
    }

    /**
     * This method is used to send the email to the user confirm his email.
     */
    public void sendEmailConfirmRegister(Player player) {
        String[] args = new String[3];
        args[0] = player.getName();
        args[1] = player.getEmail();
        args[2] = player.getEmailConfirmed();

        try {
            sendHtmlMail(false, 1, player.getEmail(), args);
        } catch (Exception e) {
            log.error("sendEmailConfirmRegister", e);
        }
    }

    /**
     * This method is used to send the email to the user when he doesn't remember his password.
     */
    public void sendEmailPasswordReset(Player player) {
        String[] args = new String[3];
        args[0] = player.getName();
        args[1] = player.getEmail();
        args[2] = player.getPasswordReset();

        try {
            sendHtmlMail(false, 2, player.getEmail(), args);
        } catch (Exception e) {
            log.error("sendEmailPasswordReset", e);
        }
    }

    /**
     * Send one email of type 3 (payment registered).
     */
    public void sendEmailPaymentRegistered(Player player, int quantity) {
        String[] args = new String[2];
        args[0] = player.getName();
        args[1] = "" + quantity;

        try {
            if (player.getEmail() != null && !player.getEmail().isEmpty()) {
                sendHtmlMail(false, 3, player.getEmail(), args);
            }
        } catch (Exception e) {
            log.error("sendEmailPaymentRegistered", e);
        }
    }

    /**
     * Send one email of type 4 (invitation to class).
     */
    public void sendEmailInvitationToClass(Player player, ClassPlayer classPlayer) {
        String[] args = new String[3];
        args[0] = classPlayer.getPlayer().getName(); // Student
        args[1] = player.getName(); // Teacher
        args[2] = classPlayer.getClazz().getName();

        try {
            sendHtmlMail(false, 4, classPlayer.getPlayer().getEmail(), args);
        } catch (Exception e) {
            log.error("sendEmailInvitationToClass", e);
        }
    }

    /**
     * Send one email of type 5 (contact).
     */
    public void sendEmailContact(String email, String message) {
        String[] args = new String[2];
        args[0] = email;
        args[1] = message;

        try {
            sendHtmlMail(false, 5, "", args);
        } catch (Exception e) {
            log.error("sendEmailContact", e);
        }
    }

    /**
     * This method is used to send the email to list of players from the group communication of type groupCommunicationId.
     */
    public void sendEmailCommunication(int groupCommunicationId, List<Player> players) {
        for (Player player : players) {
            String[] args = new String[5];
            args[0] = player.getName();
            args[1] = (player.getIdFacebook() != null ? String.valueOf(player.getIdFacebook()) : null);
            args[2] = String.valueOf(player.getCredit());
            args[3] = String.valueOf(player.getScore());
            args[4] = "(" + player.getRankLevel() + ") " + player.getRankColor();

            try {
                sendMessageCommunication(true, groupCommunicationId, player.getEmail(), args);
            } catch (Exception e) {
                log.error("sendEmailCommunication", e);
            }
        }
    }
}
