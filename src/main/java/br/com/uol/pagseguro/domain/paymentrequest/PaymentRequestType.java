package br.com.uol.pagseguro.domain.paymentrequest;

public enum PaymentRequestType {
    TRANSFER(1), SERVICES(2), PRODUCTS(3);

    private int publicCode;

    private PaymentRequestType(int publicCode) {
        this.publicCode = publicCode;
    }

    public int getPublicCode() {
        return this.publicCode;
    }

    public static PaymentRequestType fromValue(int publicCode) {
        for (PaymentRequestType value : PaymentRequestType.values()) {
            if (value.getPublicCode() == publicCode) {
                return value;
            }
        }

        return null;
    }

}
