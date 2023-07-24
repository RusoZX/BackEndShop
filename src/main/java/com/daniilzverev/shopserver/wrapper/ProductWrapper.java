package com.daniilzverev.shopserver.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductWrapper {

    //We create this class so we can send less information to the frontend
    //Like for example, when browsing products you dont need all the info from all the products.
    private Long id;

    private String title;

    private Float price;

    private Integer stock;

    private byte[] imageData;
    private String type;

    public ProductWrapper(Long id, String title, Float price, Integer stock) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.stock = stock;
    }

}
