package br.com.uol.pagseguroV2.enums;

public enum PaymentMode {

    DEFAULT("default"),

    GATEWAY("gateway");

    private final String value;

    private PaymentMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PaymentMode fromValue(String value) {
        try {
            return PaymentMode.valueOf(value.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

}
