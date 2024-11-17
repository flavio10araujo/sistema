package com.polifono.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class EmailMessageBuilderTest {

    public static final String emailCompany = "Company Name";
    public static final String emailCompanySlogan = "Test Company Slogan";
    public static final String emailUrl = "www.company.com";
    public static final String emailGeneral = "company@email.com";
    public static final String emailNoReply = "noreply@email.com";

    private static EmailMessageBuilder emailMessageBuilder;

    @BeforeAll
    public static void setUp() {
        emailMessageBuilder = new EmailMessageBuilder();
        emailMessageBuilder.setEmailCompany(emailCompany);
        emailMessageBuilder.setEmailCompanySlogan(emailCompanySlogan);
        emailMessageBuilder.setEmailUrl(emailUrl);
        emailMessageBuilder.setEmailGeneral(emailGeneral);
        emailMessageBuilder.setEmailNoReply(emailNoReply);
    }

    @Test
    public void givenMessageType_WhenGetEmailFrom_ThenReturnRightEmail() {
        assertEquals(emailNoReply, emailMessageBuilder.getEmailFrom(1));
        assertEquals(emailNoReply, emailMessageBuilder.getEmailFrom(2));
        assertEquals(emailGeneral, emailMessageBuilder.getEmailFrom(3));
        assertEquals(emailGeneral, emailMessageBuilder.getEmailFrom(4));
        assertEquals(emailGeneral, emailMessageBuilder.getEmailFrom(5));
        assertEquals(emailGeneral, emailMessageBuilder.getEmailFrom(104));
        assertEquals(emailGeneral, emailMessageBuilder.getEmailFrom(105));
        assertThrows(IllegalArgumentException.class, () -> emailMessageBuilder.getEmailFrom(999));
    }

    @Test
    public void givenMessageType_WhenGetEmailSubject_ThenReturnRightEmailSubject() {
        assertEquals("Confirme seu cadastro na " + emailCompany, emailMessageBuilder.getEmailSubject(1, null));
        assertEquals("Sua solicitação de alteração de senha na " + emailCompany, emailMessageBuilder.getEmailSubject(2, null));
        assertEquals("Sua compra de créditos foi confirmada na " + emailCompany, emailMessageBuilder.getEmailSubject(3, null));
        assertEquals("Você foi convidado para uma sala de aula na " + emailCompany, emailMessageBuilder.getEmailSubject(4, null));
        assertEquals("Novo contato: " + emailCompany, emailMessageBuilder.getEmailSubject(5, new String[] { emailCompany }));
        assertEquals("Sentimos sua falta na " + emailCompany + "... Acesse sua conta para continuar a aprender música!",
                emailMessageBuilder.getEmailSubject(104, null));
        assertEquals("Sentimos sua falta na " + emailCompany + "... Acesse sua conta para continuar a aprender música!",
                emailMessageBuilder.getEmailSubject(105, null));
        assertThrows(IllegalArgumentException.class, () -> emailMessageBuilder.getEmailSubject(999, null));
    }

    @Test
    public void givenMessageType_WhenGetEmailMessage_ThenReturnRightEmailMessage() {
        assertTrue(areStringsEquivalent(getMessageType1(), emailMessageBuilder.getEmailMessage(1, new String[] {})));
        assertTrue(areStringsEquivalent(getMessageType2(), emailMessageBuilder.getEmailMessage(2, new String[] {})));
        assertTrue(areStringsEquivalent(getMessageType3(), emailMessageBuilder.getEmailMessage(3, new String[] {})));
        assertTrue(areStringsEquivalent(getMessageType4(), emailMessageBuilder.getEmailMessage(4, new String[] {})));
        assertTrue(areStringsEquivalent(getMessageType5(), emailMessageBuilder.getEmailMessage(5, new String[] {})));
        assertTrue(areStringsEquivalent(getMessageType104(), emailMessageBuilder.getEmailMessage(104, new String[] {})));
        assertTrue(areStringsEquivalent(getMessageType105WithCredits(), emailMessageBuilder.getEmailMessage(105, new String[] { "", "", "10" })));
        assertTrue(areStringsEquivalent(getMessageType105WithoutCredits(), emailMessageBuilder.getEmailMessage(105, new String[] { "", "", "0" })));
        assertThrows(IllegalArgumentException.class, () -> emailMessageBuilder.getEmailMessage(999, new String[] {}));
    }

    public boolean areStringsEquivalent(String str1, String str2) {
        String normalizedStr1 = str1.replaceAll("\\s+", "");
        String normalizedStr2 = str2.replaceAll("\\s+", "");
        return normalizedStr1.equals(normalizedStr2);
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

    private String getMessageType105WithCredits() {
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
                                        Estamos sempre melhorando nosso sistema, criando novas funcionalidades e adicionando novas aulas.
                                    </font>
                                </p>
                                <p>
                                    <font face="arial" size="2">
                                        Você ainda tem créditos em sua conta da sua última compra!
                                    </font>
                                </p>
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
                """.formatted(emailCompany, emailCompany, emailUrl, emailUrl, emailCompany, emailCompany, emailCompanySlogan, emailUrl, emailUrl);
    }

    private String getMessageType105WithoutCredits() {
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
                                        Estamos sempre melhorando nosso sistema, criando novas funcionalidades e adicionando novas aulas.
                                    </font>
                                </p>
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
                """.formatted(emailCompany, emailCompany, emailUrl, emailUrl, emailCompany, emailCompany, emailCompanySlogan, emailUrl, emailUrl);
    }
}
