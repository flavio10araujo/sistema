package br.com.uol.pagseguroV2.enums;

import lombok.Getter;

@Getter
public enum PaymentMethodType {
    CREDIT_CARD("CREDIT CARD", 1),
    BOLETO("BOLETO", 2),
    PIX("PIX", 3),
    UNKNOWN_TYPE("UNKNOWN TYPE. SEE ONLINE DOCUMENTATION", -1);

    private String type;
    private Integer value;

    PaymentMethodType(String type, Integer value) {
        this.type = type;
        this.value = value;
    }

    public static PaymentMethodType fromValue(Integer value) {
        for (PaymentMethodType paymentMethodType : values()) {
            if (paymentMethodType.value.equals(value)) {
                return paymentMethodType;
            }
        }

        UNKNOWN_TYPE.setValue(value);
        return UNKNOWN_TYPE;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
