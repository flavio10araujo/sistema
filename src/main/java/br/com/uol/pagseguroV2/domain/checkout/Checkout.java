package br.com.uol.pagseguroV2.domain.checkout;

import br.com.uol.pagseguroV2.domain.Item;
import br.com.uol.pagseguroV2.enums.PaymentMethodType;
import br.com.uol.pagseguroV2.exception.PagSeguroServiceException;
import br.com.uol.pagseguroV2.service.checkout.CheckoutService;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a checkout request.
 * <a href="https://developer.pagbank.com.br/reference/criar-checkout">External documentation.</a>
 */
@Setter
public class Checkout {
    @Getter
    @JsonProperty("reference_id")
    private String referenceId;

    @Getter
    @JsonProperty("customer_modifiable")
    private boolean customerModifiable;

    private List<Item> items;

    @Getter
    @JsonProperty("payment_methods")
    @JsonSerialize(using = PaymentMethodTypeSerializer.class)
    private List<PaymentMethodType> paymentMethods;

    @Getter
    @JsonProperty("redirect_url")
    private String redirectURL;

    @Getter
    @JsonProperty("payment_notification_urls")
    private List<String> paymentNotificationUrls;

    public void addItem(String referenceId, String name, String description, Integer quantity, BigDecimal amount) {
        this.getItems().add(new Item(referenceId, name, description, quantity, amount));
    }

    public List<Item> getItems() {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }

        return this.items;
    }

    public String register() throws PagSeguroServiceException, IOException {
        return CheckoutService.createCheckoutRequest(this);
    }

    @Override
    public String toString() {
        return "PaymentRequest(Reference="
                + referenceId
                + ")";
    }
}
