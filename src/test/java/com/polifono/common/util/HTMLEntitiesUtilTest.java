package com.polifono.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class HTMLEntitiesUtilTest {

    @Test
    public void givenStringWithHTMLEntities_whenEncodeHtmlEntities_thenConvertHTMLEntitiesToSpecialAndExtendedUnicodeCharacters() {
        String message = """
                <table cellpadding="0" cellspacing="0" align="center" width="550" summary="">
                <thead><tr><td align="center"><font color="#7EBB3B" face="arial" size="+2" style="text-transform:uppercase"><b>" + emailCompany + "</b></font><hr size="3" color="#7EBB3B"></td></tr></thead><tbody><tr><td>
                <p><font color="#7EBB3B" face="arial" size="+1"><b>Olá {0},</b></font></p>
                <p><font face="arial" size="2">Você iniciou seu cadastro na plataforma " + emailCompany + " com o e-mail <a href="#">{1}</a>. Para finalizá-lo, precisamos que você valide seu email.</font></p>
                <p><font face="arial" size="2">Para validar:</font></p>
                <p><font face="arial" size="2">- Acesse <a href="https://" + emailUrl + "/emailconfirmation">" + emailUrl + "/emailconfirmation</a></font></p>
                <p><font face="arial" size="2">- Informe o e-mail cadastrado;</font></p>
                <p><font face="arial" size="2">- Informe o seguinte código de ativação: <b>{2}</b></font></p>
                </td></tr><tr><td>
                <font face="arial" size="-1"><br>Atenciosamente,<br>Equipe " + emailCompany + "<br /><br /><strong>" + emailCompany + "</strong> - " + emailCompanySlogan + "</font>
                </td></tr><tr><td align="center">
                <hr size="2" color="#EFEFEF"><font face="arial" size="-1">DÚVIDAS? Acesse <a href="https://" + emailUrl + "/#faq">" + emailUrl + "/#faq</a></font>
                </td></tr><tr><td align="center"><hr size="2" color="#EFEFEF"><font face="arial" size="-1">Este é um e-mail automático disparado pelo sistema. Favor não respondê-lo, pois esta conta não é monitorada.</font>
                </td></tr></tbody></table>
                """;

        String expected = """
                <table cellpadding="0" cellspacing="0" align="center" width="550" summary="">
                <thead><tr><td align="center"><font color="#7EBB3B" face="arial" size="+2" style="text-transform:uppercase"><b>" + emailCompany + "</b></font><hr size="3" color="#7EBB3B"></td></tr></thead><tbody><tr><td>
                <p><font color="#7EBB3B" face="arial" size="+1"><b>Ol&aacute; {0},</b></font></p>
                <p><font face="arial" size="2">Voc&ecirc; iniciou seu cadastro na plataforma " + emailCompany + " com o e-mail <a href="#">{1}</a>. Para finaliz&aacute;-lo, precisamos que voc&ecirc; valide seu email.</font></p>
                <p><font face="arial" size="2">Para validar:</font></p>
                <p><font face="arial" size="2">- Acesse <a href="https://" + emailUrl + "/emailconfirmation">" + emailUrl + "/emailconfirmation</a></font></p>
                <p><font face="arial" size="2">- Informe o e-mail cadastrado;</font></p>
                <p><font face="arial" size="2">- Informe o seguinte c&oacute;digo de ativa&ccedil;&atilde;o: <b>{2}</b></font></p>
                </td></tr><tr><td>
                <font face="arial" size="-1"><br>Atenciosamente,<br>Equipe " + emailCompany + "<br /><br /><strong>" + emailCompany + "</strong> - " + emailCompanySlogan + "</font>
                </td></tr><tr><td align="center">
                <hr size="2" color="#EFEFEF"><font face="arial" size="-1">D&Uacute;VIDAS? Acesse <a href="https://" + emailUrl + "/#faq">" + emailUrl + "/#faq</a></font>
                </td></tr><tr><td align="center"><hr size="2" color="#EFEFEF"><font face="arial" size="-1">Este &eacute; um e-mail autom&aacute;tico disparado pelo sistema. Favor n&atilde;o respond&ecirc;-lo, pois esta conta n&atilde;o &eacute; monitorada.</font>
                </td></tr></tbody></table>
                """;

        String actual = HTMLEntitiesUtil.encodeHtmlEntities(message);

        assertEquals(expected, actual);
    }

    @Test
    public void givenStringWithSpecialAndExtendedCharacters_whenDecodeHtmlEntities_thenConvertSpecialAndExtendedCharactersIntoHTMLEntities() {
        String message = """
                <table cellpadding="0" cellspacing="0" align="center" width="550" summary="">
                <thead><tr><td align="center"><font color="#7EBB3B" face="arial" size="+2" style="text-transform:uppercase"><b>" + emailCompany + "</b></font><hr size="3" color="#7EBB3B"></td></tr></thead><tbody><tr><td>
                <p><font color="#7EBB3B" face="arial" size="+1"><b>Ol&aacute; {0},</b></font></p>
                <p><font face="arial" size="2">Voc&ecirc; iniciou seu cadastro na plataforma " + emailCompany + " com o e-mail <a href="#">{1}</a>. Para finaliz&aacute;-lo, precisamos que voc&ecirc; valide seu email.</font></p>
                <p><font face="arial" size="2">Para validar:</font></p>
                <p><font face="arial" size="2">- Acesse <a href="https://" + emailUrl + "/emailconfirmation">" + emailUrl + "/emailconfirmation</a></font></p>
                <p><font face="arial" size="2">- Informe o e-mail cadastrado;</font></p>
                <p><font face="arial" size="2">- Informe o seguinte c&oacute;digo de ativa&ccedil;&atilde;o: <b>{2}</b></font></p>
                </td></tr><tr><td>
                <font face="arial" size="-1"><br>Atenciosamente,<br>Equipe " + emailCompany + "<br /><br /><strong>" + emailCompany + "</strong> - " + emailCompanySlogan + "</font>
                </td></tr><tr><td align="center">
                <hr size="2" color="#EFEFEF"><font face="arial" size="-1">D&Uacute;VIDAS? Acesse <a href="https://" + emailUrl + "/#faq">" + emailUrl + "/#faq</a></font>
                </td></tr><tr><td align="center"><hr size="2" color="#EFEFEF"><font face="arial" size="-1">Este &eacute; um e-mail autom&aacute;tico disparado pelo sistema. Favor n&atilde;o respond&ecirc;-lo, pois esta conta n&atilde;o &eacute; monitorada.</font>
                </td></tr></tbody></table>
                """;

        String expected = """
                <table cellpadding="0" cellspacing="0" align="center" width="550" summary="">
                <thead><tr><td align="center"><font color="#7EBB3B" face="arial" size="+2" style="text-transform:uppercase"><b>" + emailCompany + "</b></font><hr size="3" color="#7EBB3B"></td></tr></thead><tbody><tr><td>
                <p><font color="#7EBB3B" face="arial" size="+1"><b>Olá {0},</b></font></p>
                <p><font face="arial" size="2">Você iniciou seu cadastro na plataforma " + emailCompany + " com o e-mail <a href="#">{1}</a>. Para finalizá-lo, precisamos que você valide seu email.</font></p>
                <p><font face="arial" size="2">Para validar:</font></p>
                <p><font face="arial" size="2">- Acesse <a href="https://" + emailUrl + "/emailconfirmation">" + emailUrl + "/emailconfirmation</a></font></p>
                <p><font face="arial" size="2">- Informe o e-mail cadastrado;</font></p>
                <p><font face="arial" size="2">- Informe o seguinte código de ativação: <b>{2}</b></font></p>
                </td></tr><tr><td>
                <font face="arial" size="-1"><br>Atenciosamente,<br>Equipe " + emailCompany + "<br /><br /><strong>" + emailCompany + "</strong> - " + emailCompanySlogan + "</font>
                </td></tr><tr><td align="center">
                <hr size="2" color="#EFEFEF"><font face="arial" size="-1">DÚVIDAS? Acesse <a href="https://" + emailUrl + "/#faq">" + emailUrl + "/#faq</a></font>
                </td></tr><tr><td align="center"><hr size="2" color="#EFEFEF"><font face="arial" size="-1">Este é um e-mail automático disparado pelo sistema. Favor não respondê-lo, pois esta conta não é monitorada.</font>
                </td></tr></tbody></table>
                """;

        String actual = HTMLEntitiesUtil.decodeHtmlEntities(message);

        assertEquals(expected, actual);
    }
}
