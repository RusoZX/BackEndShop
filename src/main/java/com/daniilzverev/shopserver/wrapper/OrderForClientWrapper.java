package com.daniilzverev.shopserver.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderForClientWrapper {

    private Long id;

    private String paymentStatus;

    private String orderStatus;

    public OrderForClientWrapper(Long id, Boolean paymentStatus, String orderStatus) {
        this.id = id;
        this.paymentStatus = paymentStatus.toString();
        this.orderStatus = orderStatus;
    }
}
