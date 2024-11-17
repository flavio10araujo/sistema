package com.polifono.model.enums;

public enum TransactionStatus {
    PAID(3),
    AVAILABLE(4);

    private final int value;

    TransactionStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

