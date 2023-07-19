package com.daniilzverev.shopserver.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartWrapper {
    private Long id;
    private Long productId;
    private String title;
    private Float price;
    private Integer stock;
    private Integer quantity;

    public CartWrapper(Long id,Long productId, String title, Float price, Integer stock, Integer quantity) {
        this.id = id;
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.stock = stock;
        this.quantity = quantity;
    }
}
