package br.com.uol.pagseguroV2.domain;

import br.com.uol.pagseguroV2.exception.PagSeguroServiceException;
import br.com.uol.pagseguroV2.properties.PagSeguroConfig;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class AccountCredentials extends Credentials {

    private static final int HASH_SIZE = 3;
    @Setter @Getter private String email;
    private String productionToken;
    private String sandboxToken;

    public AccountCredentials(String email, String productionToken, String sandboxToken) throws PagSeguroServiceException {
        if (email == null || "".equals(email.trim()) || productionToken == null || "".equals(productionToken.trim()) || sandboxToken == null || "".equals(
                sandboxToken.trim())) {
            throw new PagSeguroServiceException("Credentials not set.");
        }

        this.email = email.trim();
        this.productionToken = productionToken.trim();
        this.sandboxToken = sandboxToken.trim();
    }

    public String getToken() {
        if (PagSeguroConfig.isSandboxEnvironment()) {
            return this.sandboxToken;
        }

        return this.productionToken;
    }

    @Override
    public Map<Object, Object> getAttributes() throws PagSeguroServiceException {

        Map<Object, Object> attributeMap = new HashMap<>(HASH_SIZE);

        attributeMap.put("email", this.email);
        if (PagSeguroConfig.isSandboxEnvironment()) {
            if (this.sandboxToken == null || "".equals(this.sandboxToken)) {
                throw new PagSeguroServiceException("Sandbox credentials not set.");
            }
            attributeMap.put("token", this.sandboxToken);
        } else {
            attributeMap.put("token", this.productionToken);
        }

        return attributeMap;

    }

    @Override
    public String toString() {
        return this.email + " - " + this.productionToken + " (production token) - " + this.sandboxToken + " (sandbox token)";
    }
}
