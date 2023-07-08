package com.daniilzverev.shopserver.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartWrapper {
    private Long id;
    private String title;
    private Float price;
    private Integer quantity;

    public CartWrapper(Long id, String title, Float price, Integer quantity) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }
}
