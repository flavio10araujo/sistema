package com.polifono.common.util;

import java.util.HashMap;
import java.util.Map;

public class EmailDomainCorrector {

    private static final Map<String, String> DOMAIN_CORRECTIONS = new HashMap<>();

    static {
        DOMAIN_CORRECTIONS.put("@gmail.com.br", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmail.co", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmail.comm", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gamail.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gamil.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gemeil.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gemil.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gimal.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmai.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmaiil.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmaio.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmakl.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmeil.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmil.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmial.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gnail.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@g-mail.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gemail.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmail.cm", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmal.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmaill.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmail.om", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmail.org", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmail.comk", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmail.ccom", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmais.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gma.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@gmail.vom", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@fmail.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@hmail.com", "@gmail.com");
        DOMAIN_CORRECTIONS.put("@hotmail.co", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotmail.comm", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hitmail.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@homail.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hot.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotail.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotamil.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotimail.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotmaeil.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotmayl.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotmsil.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hptmail.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@htmail.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@rotmail.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotmil.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotnail.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotmal.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotmaill.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotmailk.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hormail.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotmaol.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@jotmail.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hotmai.com", "@hotmail.com");
        DOMAIN_CORRECTIONS.put("@hitmail.com.br", "@hotmail.com.br");
        DOMAIN_CORRECTIONS.put("@homail.com.br", "@hotmail.com.br");
        DOMAIN_CORRECTIONS.put("@hot.com.br", "@hotmail.com.br");
        DOMAIN_CORRECTIONS.put("@hotail.com.br", "@hotmail.com.br");
        DOMAIN_CORRECTIONS.put("@hotamil.com.br", "@hotmail.com.br");
        DOMAIN_CORRECTIONS.put("@hotimail.com.br", "@hotmail.com.br");
        DOMAIN_CORRECTIONS.put("@hotmaeil.com.br", "@hotmail.com.br");
        DOMAIN_CORRECTIONS.put("@hotmayl.com.br", "@hotmail.com.br");
        DOMAIN_CORRECTIONS.put("@hotmsil.com.br", "@hotmail.com.br");
        DOMAIN_CORRECTIONS.put("@hptmail.com.br", "@hotmail.com.br");
        DOMAIN_CORRECTIONS.put("@htmail.com.br", "@hotmail.com.br");
        DOMAIN_CORRECTIONS.put("@rotmail.com.br", "@hotmail.com.br");
        DOMAIN_CORRECTIONS.put("@hotmil.com.br", "@hotmail.com.br");
        DOMAIN_CORRECTIONS.put("@yaho.com.br", "@yahoo.com.br");
        DOMAIN_CORRECTIONS.put("@yhoo.com.br", "@yahoo.com.br");
        DOMAIN_CORRECTIONS.put("@uahoo.com.br", "@yahoo.com.br");
        DOMAIN_CORRECTIONS.put("@yahoo.co.br", "@yahoo.com.br");
        DOMAIN_CORRECTIONS.put("@yahoo.vom.br", "@yahoo.com.br");
        DOMAIN_CORRECTIONS.put("@ahoo.com.br", "@yahoo.com.br");
        DOMAIN_CORRECTIONS.put("@yaho.com", "@yahoo.com");
        DOMAIN_CORRECTIONS.put("@yhoo.com", "@yahoo.com");
        DOMAIN_CORRECTIONS.put("@uahoo.com", "@yahoo.com");
        DOMAIN_CORRECTIONS.put("@boll.com.br", "@bol.com.br");
        DOMAIN_CORRECTIONS.put("@bl.com.br", "@bol.com.br");
        DOMAIN_CORRECTIONS.put("@bol.co.br", "@bol.com.br");
        DOMAIN_CORRECTIONS.put("@outllok.com", "@outlook.com");
        DOMAIN_CORRECTIONS.put("@outluoock.com", "@outlook.com");
        DOMAIN_CORRECTIONS.put("@otlook.com", "@outlook.com");
        DOMAIN_CORRECTIONS.put("@aiclod.com", "@icloud.com");
    }

    public static String correctDomain(String email) {
        if (email == null || email.isEmpty()) {
            return email;
        }

        int indexAt = email.indexOf("@");

        if (indexAt < 0) {
            return email;
        }

        email = email.toLowerCase();
        String domain = email.substring(indexAt);

        if (DOMAIN_CORRECTIONS.containsKey(domain)) {
            return email.replaceAll(domain, DOMAIN_CORRECTIONS.get(domain));
        }

        return email;
    }
}
