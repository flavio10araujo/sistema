package com.polifono.util;

import java.util.List;

import org.apache.commons.mail.MultiPartEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.polifono.domain.ClassPlayer;
import com.polifono.domain.Player;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EmailSendUtil {

    private static String emailLogin;
    private static String emailPassword;
    private static String emailHostName;
    private static String emailSmtpPort;
    private static String emailCharset;
    private static String emailCompany;
    private static String emailCompanySlogan;
    private static String emailUrl;
    private static String emailGeneral;
    private static String emailNoReply;
    private static String emailGeneralTo;

    @Value("${app.email.authentication.login}")
    public void setEmailLogin(String emailLogin) {
        EmailSendUtil.emailLogin = emailLogin;
    }

    @Value("${app.email.authentication.password}")
    public void setEmailPassword(String emailPassword) {
        EmailSendUtil.emailPassword = emailPassword;
    }

    @Value("${app.email.hostName}")
    public void setEmailHostName(String emailHostName) {
        EmailSendUtil.emailHostName = emailHostName;
    }

    @Value("${app.email.smtpPort}")
    public void setEmailSmtpPort(String emailSmtpPort) {
        EmailSendUtil.emailSmtpPort = emailSmtpPort;
    }

    @Value("${app.email.charset}")
    public void setEmailCharset(String emailCharset) {
        EmailSendUtil.emailCharset = emailCharset;
    }

    @Value("${app.general.name}")
    public void setEmailCompany(String emailCompany) {
        EmailSendUtil.emailCompany = emailCompany;
    }

    @Value("${app.general.slogan}")
    public void setEmailCompanySlogan(String emailCompanySlogan) {
        EmailSendUtil.emailCompanySlogan = emailCompanySlogan;
    }

    @Value("${app.general.url}")
    public void setEmailUrl(String emailUrl) {
        EmailSendUtil.emailUrl = emailUrl;
    }

    @Value("${app.email.accounts.general.address}")
    public void setEmailGeneral(String emailGeneral) {
        EmailSendUtil.emailGeneral = emailGeneral;
    }

    @Value("${app.email.accounts.noReply.address}")
    public void setEmailNoReply(String emailNoReply) {
        EmailSendUtil.emailNoReply = emailNoReply;
    }

    @Value("${app.email.accounts.general.to}")
    public void setEmailGeneralTo(String emailGeneralTo) {
        EmailSendUtil.emailGeneralTo = emailGeneralTo;
    }

    /**
     * The class MailThread can send an email asynchronously.
     * If it is used this method, the method that called this method can not know if the email was sent.
     */
    static class MailAsync extends Thread {

        private final String senderAddress;
        private final String recipientAddress;
        private final String subject;
        private String message;

        public MailAsync(String senderAddress, String subject, String message, String recipientAddress) {
            this.senderAddress = senderAddress;
            this.subject = subject;
            this.message = message;
            this.recipientAddress = recipientAddress;
        }

        @Override
        public void run() {
            try {
                MultiPartEmail hm = new MultiPartEmail();

                hm.setHostName(emailHostName);
                hm.setSmtpPort(Integer.parseInt(emailSmtpPort));
                hm.setAuthentication(emailLogin, emailPassword);
                hm.setCharset(emailCharset);
                hm.setSubject(subject);
                hm.setFrom(senderAddress);
                hm.addTo(recipientAddress);

                message = HTMLEntitiesUtil.decodeHtmlEntities(message);
                message = HTMLEntitiesUtil.encodeHtmlEntities(message);

                hm.addPart(message, org.apache.commons.mail.Email.TEXT_HTML);

                hm.send();

                System.out.println("E-mail enviado!");
            } catch (Exception e) {
                log.error("An email was not sent: ", e);
                throw new RuntimeException("An email was not sent: ", e);
            }
        }
    }

    static class MailSync {

        private final String senderAddress;
        private final String recipientAddress;
        private final String subject;
        private String message;

        public MailSync(String senderAddress, String subject, String message, String recipientAddress) {
            this.senderAddress = senderAddress;
            this.subject = subject;
            this.message = message;
            this.recipientAddress = recipientAddress;
        }

        public void start() {
            try {
                MultiPartEmail hm = new MultiPartEmail();

                hm.setHostName(emailHostName);
                hm.setSmtpPort(Integer.parseInt(emailSmtpPort));
                hm.setAuthentication(emailLogin, emailPassword);
                hm.setCharset(emailCharset);
                hm.setSubject(subject);
                hm.setFrom(senderAddress);
                hm.addTo(recipientAddress);

                message = HTMLEntitiesUtil.decodeHtmlEntities(message);
                message = HTMLEntitiesUtil.encodeHtmlEntities(message);

                hm.addPart(message, org.apache.commons.mail.Email.TEXT_HTML);

                hm.send();
            } catch (Exception e) {
                log.error("An email was not sent: ", e);
                throw new RuntimeException("An email was not sent: ", e);
            }
        }
    }

    private static void sendHtmlMail(boolean async, int messageType, String to, String[] args) {
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

        if (async) {
            new EmailSendUtil.MailAsync(from, subject, message, to).start();
        } else {
            new EmailSendUtil.MailSync(from, subject, message, to).start();
        }
    }

    private static void sendMessageCommunication(boolean async, int messageType, String to, String[] args) {
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

        if (async) {
            new EmailSendUtil.MailAsync(from, subject, message, to).start();
        } else {
            new EmailSendUtil.MailSync(from, subject, message, to).start();
        }
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
    private static String replaceParamsMessage(String message, String[] args) {
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
    public static void sendEmailConfirmRegister(Player player) {
        String[] args = new String[3];
        args[0] = player.getName();
        args[1] = player.getEmail();
        args[2] = player.getEmailConfirmed();

        try {
            sendHtmlMail(true, 1, player.getEmail(), args);
        } catch (Exception e) {
            log.error("sendEmailConfirmRegister", e);
        }
    }

    /**
     * This method is used to send the email to the user when he doesn't remember his password.
     */
    public static void sendEmailPasswordReset(Player player) {
        String[] args = new String[3];
        args[0] = player.getName();
        args[1] = player.getEmail();
        args[2] = player.getPasswordReset();

        try {
            sendHtmlMail(true, 2, player.getEmail(), args);
        } catch (Exception e) {
            log.error("sendEmailPasswordReset", e);
        }
    }

    /**
     * Send one email of type 3 (payment registered).
     */
    public static void sendEmailPaymentRegistered(Player player, int quantity) {
        String[] args = new String[2];
        args[0] = player.getName();
        args[1] = "" + quantity;

        try {
            if (player.getEmail() != null && !player.getEmail().isEmpty()) {
                sendHtmlMail(true, 3, player.getEmail(), args);
            }
        } catch (Exception e) {
            log.error("sendEmailPaymentRegistered", e);
        }
    }

    /**
     * Send one email of type 4 (invitation to class).
     */
    public static void sendEmailInvitationToClass(Player player, ClassPlayer classPlayer) {
        String[] args = new String[3];
        args[0] = classPlayer.getPlayer().getName(); // Student
        args[1] = player.getName(); // Teacher
        args[2] = classPlayer.getClazz().getName();

        try {
            sendHtmlMail(true, 4, classPlayer.getPlayer().getEmail(), args);
        } catch (Exception e) {
            log.error("sendEmailInvitationToClass", e);
        }
    }

    /**
     * Send one email of type 5 (contact).
     */
    public static void sendEmailContact(String email, String message) {
        String[] args = new String[2];
        args[0] = email;
        args[1] = message;

        try {
            sendHtmlMail(true, 5, "", args);
        } catch (Exception e) {
            log.error("sendEmailContact", e);
        }
    }

    /**
     * This method is used to send the email to list of players from the group communication of type groupCommunicationId.
     */
    public static void sendEmailCommunication(int groupCommunicationId, List<Player> players) {
        for (Player player : players) {
            String[] args = new String[5];
            args[0] = player.getName();
            args[1] = (player.getIdFacebook() != null ? String.valueOf(player.getIdFacebook()) : null);
            args[2] = String.valueOf(player.getCredit());
            args[3] = String.valueOf(player.getScore());
            args[4] = "(" + player.getRankLevel() + ") " + player.getRankColor();

            try {
                sendMessageCommunication(false, groupCommunicationId, player.getEmail(), args);
            } catch (Exception e) {
                log.error("sendEmailCommunication", e);
            }
        }
    }
}
