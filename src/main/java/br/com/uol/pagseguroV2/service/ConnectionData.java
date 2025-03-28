package br.com.uol.pagseguroV2.service;

import br.com.uol.pagseguroV2.domain.Credentials;
import br.com.uol.pagseguroV2.exception.PagSeguroServiceException;
import br.com.uol.pagseguroV2.helper.PagSeguroUtil;
import br.com.uol.pagseguroV2.properties.PagSeguroV2System;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ConnectionData {

    @Setter private Credentials credentials;
    private String webServiceUrl;

    public ConnectionData(Credentials credentials) {
        this.credentials = credentials;
        this.webServiceUrl = validUrlWebService();
    }

    public String getCredentialsUrlQuery() throws PagSeguroServiceException {
        return PagSeguroUtil.urlQuery(this.getCredentials().getAttributes());
    }

    private String validUrlWebService() {
        return PagSeguroV2System.getUrlProduction() + PagSeguroV2System.getCheckoutServicePath();
    }
}
