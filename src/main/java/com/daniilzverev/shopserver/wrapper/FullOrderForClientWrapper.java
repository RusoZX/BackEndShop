package com.daniilzverev.shopserver.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class FullOrderForClientWrapper {
    private boolean paymentStatus;

    private String orderStatus;

    private String paymentMethod;

    private String deliveryMethod;

    private LocalDate dateCreated;

    private Long addressId;

    public FullOrderForClientWrapper(boolean paymentStatus, String orderStatus, String paymentMethod,String deliveryMethod, LocalDate dateCreated, Long addressId) {
        this.paymentStatus = paymentStatus;
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.deliveryMethod = deliveryMethod;
        this.dateCreated = dateCreated;
        this.addressId = addressId;
    }
}
