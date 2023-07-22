package com.daniilzverev.shopserver.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class OrderForClientWrapper {

    private Long id;

    private String paymentStatus;

    private String orderStatus;

    private LocalDate dateCreated;

    public OrderForClientWrapper(Long id, Boolean paymentStatus, String orderStatus, LocalDate dateCreated) {
        this.id = id;
        this.paymentStatus = paymentStatus.toString();
        this.orderStatus = orderStatus;
        this.dateCreated= dateCreated;
    }
}
