package br.com.uol.pagseguroV2.domain.checkout;

import br.com.uol.pagseguroV2.domain.Credentials;
import br.com.uol.pagseguroV2.domain.Item;
import br.com.uol.pagseguroV2.enums.PaymentMethodType;
import br.com.uol.pagseguroV2.exception.PagSeguroServiceException;
import br.com.uol.pagseguroV2.service.checkout.CheckoutService;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a checkout request.
 * <a href="https://developer.pagbank.com.br/reference/criar-checkout">External documentation.</a>
 */
@Setter
public class Checkout {
    @Getter private String referenceId;
    @Getter private boolean customerModifiable;
    private List<Item> items;
    @Getter private List<PaymentMethodType> paymentMethods;
    @Getter private String redirectURL;
    @Getter private List<String> paymentNotificationUrls;

    public void addItem(String referenceId, String name, String description, Integer quantity, BigDecimal amount) {
        this.getItems().add(new Item(referenceId, name, description, quantity, amount));
    }

    public List<Item> getItems() {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }

        return this.items;
    }

    public String register(Credentials credentials) throws PagSeguroServiceException {
        return this.register(credentials, false);
    }

    public String register(Credentials credentials, Boolean onlyCheckoutCode) throws PagSeguroServiceException {
        return CheckoutService.createCheckoutRequest(credentials, this, onlyCheckoutCode);
    }

    @Override
    public String toString() {
        return "PaymentRequest(Reference="
                + referenceId
                + ")";
    }
}
