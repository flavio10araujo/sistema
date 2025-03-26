package br.com.uol.pagseguroV2.service;

import br.com.uol.pagseguroV2.domain.Credentials;
import br.com.uol.pagseguroV2.exception.PagSeguroServiceException;
import br.com.uol.pagseguroV2.helper.PagSeguroUtil;
import br.com.uol.pagseguroV2.properties.PagSeguroConfig;
import br.com.uol.pagseguroV2.properties.PagSeguroSystem;
import lombok.Getter;
import lombok.Setter;

public class ConnectionData {

    @Setter @Getter private Credentials credentials;
    @Getter private String webServiceUrl;
    private String directPayment;
    @Getter private String authorizationUrl;
    @Getter private String serviceTimeout;
    @Setter @Getter private String charset;

    public ConnectionData(Credentials credentials) {
        this.credentials = credentials;
        this.webServiceUrl = validUrlWebService();
        this.charset = PagSeguroConfig.getApplicationCharset();
        this.serviceTimeout = PagSeguroSystem.getServiceTimeout();
        this.directPayment = PagSeguroSystem.getUrlDirectPayment();
        this.authorizationUrl = PagSeguroSystem.getUrlAuthorization();
    }

    public String getDirectPaymentUrl() {
        return this.directPayment;
    }

    public String getCredentialsUrlQuery() throws PagSeguroServiceException {
        return PagSeguroUtil.urlQuery(this.getCredentials().getAttributes());
    }

    private String validUrlWebService() {
        return PagSeguroSystem.getUrlProduction() + PagSeguroSystem.getCheckoutServicePath();
    }

    public String getCheckoutUrl() {
        return PagSeguroSystem.getCheckoutUrl();
    }
}
