package com.polifono.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Setter;

@Setter
@Component
public class EmailMessageBuilder {

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

    public String getEmailFrom(int messageType) {
        return switch (messageType) {
            case 1, 2 -> emailNoReply;
            case 3, 4, 5, 104, 105 -> emailGeneral;
            default -> throw new IllegalArgumentException("Invalid message type");
        };
    }

    public String getEmailSubject(int messageType, String[] args) {
        return switch (messageType) {
            case 1 -> "Confirme seu cadastro na " + emailCompany;
            case 2 -> "Sua solicitação de alteração de senha na " + emailCompany;
            case 3 -> "Sua compra de créditos foi confirmada na " + emailCompany;
            case 4 -> "Você foi convidado para uma sala de aula na " + emailCompany;
            case 5 -> "Novo contato: " + args[0];
            case 104, 105 -> "Sentimos sua falta na " + emailCompany + "... Acesse sua conta para continuar a aprender música!";
            default -> throw new IllegalArgumentException("Invalid message type");
        };
    }

    public String getEmailMessage(int messageType, String[] args) {
        return switch (messageType) {
            case 1 -> getMessageType1();
            case 2 -> getMessageType2();
            case 3 -> getMessageType3();
            case 4 -> getMessageType4();
            case 5 -> getMessageType5();
            case 104 -> getMessageType104();
            case 105 -> getMessageType105(args);
            default -> throw new IllegalArgumentException("Invalid message type");
        };
    }

    private String getMessageType1() {
        return """
                <table cellpadding="0" cellspacing="0" align="center" width="550" summary="">
                    <thead>
                        <tr>
                            <td align="center">
                                <font color="#7EBB3B" face="arial" size="+2" style="text-transform:uppercase">
                                    <b>%s</b>
                                </font>
                                <hr size="3" color="#7EBB3B">
                            </td>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>
                                <p>
                                    <font color="#7EBB3B" face="arial" size="+1">
                                        <b>Olá {0},</b>
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        Você iniciou seu cadastro na plataforma %s com o e-mail <a href="#"> {1} </a>. Para finalizá-lo, precisamos que você valide seu email.
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">Para validar:</font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        - Acesse <a href="https://%s/emailconfirmation">%s/emailconfirmation</a>
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">- Informe o e-mail cadastrado;</font>
                                </p>
                                <p>
                                    <font face="arial" size="2">- Informe o seguinte código de ativação: <b>{2}</b></font>
                                </p>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <font face="arial" size="-1">
                                    <br />Atenciosamente,<br />Equipe %s<br /><br /><strong>%s</strong> - %s
                                </font>
                            </td>
                        </tr>
                        <tr>
                            <td align="center">
                                <hr size="2" color="#EFEFEF">
                                <font face="arial" size="-1">
                                    DÚVIDAS? Acesse <a href="https://%s/#faq">%s/#faq</a>
                                </font>
                            </td>
                        </tr>
                        <tr>
                            <td align="center">
                                <hr size="2" color="#EFEFEF">
                                <font face="arial" size="-1">
                                    Este é um e-mail automático disparado pelo sistema. Favor não respondê-lo, pois esta conta não é monitorada.
                                </font>
                            </td>
                        </tr>
                    </tbody>
                </table>
                """.formatted(emailCompany, emailCompany, emailUrl, emailUrl, emailCompany, emailCompany, emailCompanySlogan, emailUrl, emailUrl);
    }

    private String getMessageType2() {
        return """
                <table cellpadding="0" cellspacing="0" align="center" width="550" summary="">
                    <thead>
                        <tr>
                            <td align="center">
                                <font color="#7EBB3B" face="arial" size="+2" style="text-transform:uppercase">
                                    <b>%s</b>
                                </font>
                                <hr size="3" color="#7EBB3B">
                            </td>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>
                                <p>
                                    <font color="#7EBB3B" face="arial" size="+1">
                                        <b>Olá {0},</b>
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        Você (ou alguém) solicitou a alteração da senha na plataforma %s para o login <a href="#"> {1} </a>.
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">Para dar continuidade na alteração da senha:</font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        - Acesse <a href="https://%s">%s</a>
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">- Clique no botão [ENTRAR];</font>
                                </p>
                                <p>
                                    <font face="arial" size="2">- Clique no link [Esqueci minha senha];</font>
                                </p>
                                <p>
                                    <font face="arial" size="2">- Informe o login cadastrado;</font>
                                </p>
                                <p>
                                    <font face="arial" size="2">- Informe o seguinte código de confirmação da alteração da senha: <b>{2}</b></font>
                                </p>
                                <p>
                                    <font face="arial" size="2">- Informe a nova senha desejada.</font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        <br /><br />Caso a alteração de senha não seja mais necessária, apenas ignore este e-mail.
                                    </font>
                                </p>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <font face="arial" size="-1">
                                    <br />Atenciosamente,<br />Equipe %s<br /><br /><strong>%s</strong> - %s
                                </font>
                            </td>
                        </tr>
                        <tr>
                            <td align="center">
                                <hr size="2" color="#EFEFEF">
                                <font face="arial" size="-1">
                                    DÚVIDAS? Acesse <a href="https://%s/#faq">%s/#faq</a>
                                </font>
                            </td>
                        </tr>
                        <tr>
                            <td align="center">
                                <hr size="2" color="#EFEFEF">
                                <font face="arial" size="-1">
                                    Este é um e-mail automático disparado pelo sistema. Favor não respondê-lo, pois esta conta não é monitorada.
                                </font>
                            </td>
                        </tr>
                    </tbody>
                </table>
                """.formatted(emailCompany, emailCompany, emailUrl, emailUrl, emailCompany, emailCompany, emailCompanySlogan, emailUrl, emailUrl);
    }

    private String getMessageType3() {
        return """
                <table cellpadding="0" cellspacing="0" align="center" width="550" summary="">
                    <thead>
                        <tr>
                            <td align="center">
                                <font color="#7EBB3B" face="arial" size="+2" style="text-transform:uppercase">
                                    <b>%s</b>
                                </font>
                                <hr size="3" color="#7EBB3B">
                            </td>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>
                                <p>
                                    <font color="#7EBB3B" face="arial" size="+1">
                                        <b>Olá {0},</b>
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        A sua compra foi confirmada e os créditos adquiridos já estão disponíveis em sua conta.
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        <br />O total de créditos adquiridos foi de: <b>{1}</b>
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        Obs.: caso esteja logado no sistema, é necessário deslogar e logar novamente para que os novos créditos sejam mostrados.
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        <br /><br />Não perca tempo e comece a estudar agora mesmo.
                                    </font>
                                </p>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <font face="arial" size="-1">
                                    <br />Atenciosamente,<br />Equipe %s<br /><br /><strong>%s</strong> - %s
                                </font>
                            </td>
                        </tr>
                        <tr>
                            <td align="center">
                                <hr size="2" color="#EFEFEF">
                                <font face="arial" size="-1">
                                    DÚVIDAS? Acesse <a href="https://%s/#faq">%s/#faq</a>
                                </font>
                            </td>
                        </tr>
                        <tr>
                            <td align="center">
                                <hr size="2" color="#EFEFEF">
                            </td>
                        </tr>
                    </tbody>
                </table>
                """.formatted(emailCompany, emailCompany, emailCompany, emailCompanySlogan, emailUrl, emailUrl);
    }

    private String getMessageType4() {
        return """
                <table cellpadding="0" cellspacing="0" align="center" width="550" summary="">
                    <thead>
                        <tr>
                            <td align="center">
                                <font color="#7EBB3B" face="arial" size="+2" style="text-transform:uppercase">
                                    <b>%s</b>
                                </font>
                                <hr size="3" color="#7EBB3B">
                            </td>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>
                                <p>
                                    <font color="#7EBB3B" face="arial" size="+1">
                                        <b>Olá {0},</b>
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        O professor {1} te adicionou na sala de aula {2}.
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        <br />Caso você queira participar dessa sala de aula, acesse o seguinte link para confirmar sua participação:
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        <br /><br /><a href="https://%s/classinvitation">%s/classinvitation</a>
                                    </font>
                                </p>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <font face="arial" size="-1">
                                    <br />Atenciosamente,<br />Equipe %s<br /><br /><strong>%s</strong> - %s
                                </font>
                            </td>
                        </tr>
                        <tr>
                            <td align="center">
                                <hr size="2" color="#EFEFEF">
                                <font face="arial" size="-1">
                                    DÚVIDAS? Acesse <a href="https://%s/#faq">%s/#faq</a>
                                </font>
                            </td>
                        </tr>
                        <tr>
                            <td align="center">
                                <hr size="2" color="#EFEFEF">
                            </td>
                        </tr>
                    </tbody>
                </table>
                """.formatted(emailCompany, emailUrl, emailUrl, emailCompany, emailCompany, emailCompanySlogan, emailUrl, emailUrl);
    }

    private String getMessageType5() {
        return """
                <table cellpadding="0" cellspacing="0" align="center" width="550" summary="">
                    <thead>
                        <tr>
                            <td align="center">
                                <font color="#7EBB3B" face="arial" size="+2" style="text-transform:uppercase">
                                    <b>%s</b>
                                </font>
                                <hr size="3" color="#7EBB3B">
                            </td>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>
                                <p>
                                    <font color="#7EBB3B" face="arial" size="+1">
                                        <b>A seguinte mensagem foi enviada por {0}:</b>
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">Mensagem: {1}</font>
                                </p>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <font face="arial" size="-1">
                                    <br />Atenciosamente,<br />Equipe %s<br /><br /><strong>%s</strong> - %s
                                </font>
                            </td>
                        </tr>
                        <tr>
                            <td align="center">
                                <hr size="2" color="#EFEFEF">
                                <font face="arial" size="-1">
                                    DÚVIDAS? Acesse <a href="https://%s/#faq">%s/#faq</a>
                                </font>
                            </td>
                        </tr>
                        <tr>
                            <td align="center">
                                <hr size="2" color="#EFEFEF">
                            </td>
                        </tr>
                    </tbody>
                </table>
                """.formatted(emailCompany, emailCompany, emailCompany, emailCompanySlogan, emailUrl, emailUrl);
    }

    private String getMessageType104() {
        return """
                <table cellpadding="0" cellspacing="0" align="center" width="550" summary="">
                    <thead>
                        <tr>
                            <td align="center">
                                <font color="#7EBB3B" face="arial" size="+2" style="text-transform:uppercase">
                                    <b>%s</b>
                                </font>
                                <hr size="3" color="#7EBB3B">
                            </td>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>
                                <p>
                                    <font color="#7EBB3B" face="arial" size="+1">
                                        <b>Olá {0},</b>
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        Sentimos sua falta! Já faz tempo que você não acessa a %s!
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        Estamos sempre melhorando nosso sistema e já temos cursos de trompete, violão, saxofone, flauta doce e teoria musical.
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        Para continuar a estudar música com a gente:
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        - Acesse <a href="https://%s">%s</a>
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        - Clique no botão [ENTRAR] (caso esteja acessando com celular, antes é preciso clicar no botão para mostrar as opções);
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        - Informe o seu email e sua senha e clique no botão [ENTRAR];
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        - Caso tenha esquecido a sua senha, faça:
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        - - Clique no link [Esqueci minha senha];
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        - - Informe o seu email;
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        - - Clique no botão [ENVIAR] e você receberá um email com as instruções para a troca da senha.
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        <br />Após acessar o sistema, escolha o curso de música que você deseja fazer e bons estudos!
                                    </font>
                                </p>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <font face="arial" size="-1">
                                    <br />Aguardamos você!<br />Equipe %s<br /><br /><strong>%s</strong> - %s
                                </font>
                            </td>
                        </tr>
                        <tr>
                            <td align="center">
                                <hr size="2" color="#EFEFEF">
                                <font face="arial" size="-1">
                                    DÚVIDAS? Acesse <a href="https://%s/#faq">%s/#faq</a>
                                </font>
                            </td>
                        </tr>
                        <tr>
                            <td align="center">
                                <hr size="2" color="#EFEFEF">
                                <font face="arial" size="-1"></font>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <p>
                                    <font color="#7EBB3B" face="arial" size="+1">
                                        <b>Promoções e novidades:</b>
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        Siga-nos nas redes sociais para aproveitar as promoções e poder fazer aulas de música de graça!
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        <a href="https://www.facebook.com/polifonooficial/">Facebook</a> - <a href="https://www.youtube.com/c/PolifonoOficial">Youtube</a> - <a href="https://www.instagram.com/polifono_music/">Instagram</a>
                                    </font>
                                </p>
                            </td>
                        </tr>
                    </tbody>
                </table>
                """.formatted(emailCompany, emailCompany, emailUrl, emailUrl, emailCompany, emailCompany, emailCompanySlogan, emailUrl, emailUrl);
    }

    private String getMessageType105(String[] args) {
        String message = """
                <table cellpadding="0" cellspacing="0" align="center" width="550" summary="">
                    <thead>
                        <tr>
                            <td align="center">
                                <font color="#7EBB3B" face="arial" size="+2" style="text-transform:uppercase">
                                    <b>%s</b>
                                </font>
                                <hr size="3" color="#7EBB3B">
                            </td>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>
                                <p>
                                    <font color="#7EBB3B" face="arial" size="+1">
                                        <b>Olá {0},</b>
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        Sentimos sua falta! Já faz tempo que você não acessa a %s!
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        Estamos sempre melhorando nosso sistema, criando novas funcionalidades e adicionando novas aulas.
                                    </font>
                                </p>
                """.formatted(emailCompany, emailCompany);

        // Credits > 0
        if (Integer.parseInt(args[2]) > 0) {
            message = message + """
                            <p>
                                <font face="arial" size="2">
                                    Você ainda tem créditos em sua conta da sua última compra!
                                </font>
                            </p>
                    """;
        }

        return message + """
                                <p>
                                    <font face="arial" size="2">
                                        Mini relatório do aluno:
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        - Créditos em conta: {2}
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        - Pontuação total: {3}
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        - Nível musical: {4}
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        <br />
                                        Acesse <a href="https://%s">%s</a> e continue estudando para subir de nível!
                                    </font>
                                </p>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <font face="arial" size="-1">
                                    <br />
                                    Aguardamos você!
                                    <br />
                                    Equipe %s
                                    <br /><br />
                                    <strong>%s</strong> - %s
                                </font>
                            </td>
                        </tr>
                        <tr>
                            <td align="center">
                                <hr size="2" color="#EFEFEF">
                                <font face="arial" size="-1">
                                    DÚVIDAS? Acesse <a href="https://%s/#faq">%s/#faq</a>
                                </font>
                            </td>
                        </tr>
                        <tr>
                            <td align="center">
                                <hr size="2" color="#EFEFEF">
                                <font face="arial" size="-1"></font>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <p>
                                    <font color="#7EBB3B" face="arial" size="+1">
                                        <b>Promoções e novidades:</b>
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        Siga-nos nas redes sociais para aproveitar as promoções e ganhar créditos!
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        <a href="https://www.facebook.com/polifonooficial/">Facebook</a> - <a href="https://www.youtube.com/c/PolifonoOficial">Youtube</a> - <a href="https://www.instagram.com/polifono_music/">Instagram</a>
                                    </font>
                                </p>
                            </td>
                        </tr>
                    </tbody>
                </table>
                """.formatted(emailUrl, emailUrl, emailCompany, emailCompany, emailCompanySlogan, emailUrl, emailUrl);
    }
}
