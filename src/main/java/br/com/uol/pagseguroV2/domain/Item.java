package br.com.uol.pagseguroV2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class Item {
    @JsonProperty("reference_id")

    private String referenceId;
    private String name;
    private String description;
    private Integer quantity;

    @JsonProperty("unit_amount")
    private BigDecimal amount;

    public Item(String referenceId, String name, String description, Integer quantity, BigDecimal amount) {
        this.referenceId = referenceId;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.amount = amount;
    }
}
