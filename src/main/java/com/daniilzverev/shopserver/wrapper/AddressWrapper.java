package com.daniilzverev.shopserver.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressWrapper {

    private Long id;

    private String city;

    private String street;
    public AddressWrapper(Long id, String city, String street){
        this.id= id;
        this.city = city;
        this.street = street;
    }
}
