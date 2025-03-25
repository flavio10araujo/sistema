package br.com.uol.pagseguroV2.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class Item {
    private String referenceId;
    private String name;
    private String description;
    private Integer quantity;
    private BigDecimal amount;

    public Item(String referenceId, String name, String description, Integer quantity, BigDecimal amount) {
        this.referenceId = referenceId;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.amount = amount;
    }
}
