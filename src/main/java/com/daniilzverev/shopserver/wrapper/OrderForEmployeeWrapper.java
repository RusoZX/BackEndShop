package com.daniilzverev.shopserver.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderForEmployeeWrapper {

    private Long id;

    private String userName;

    private String paymentStatus;

    private String orderStatus;

    private Double totalRevenue;

    public OrderForEmployeeWrapper(Long id,String userName, Boolean paymentStatus, String orderStatus, Double totalRevenue) {
        this.id = id;
        this.userName =userName;
        this.paymentStatus = paymentStatus.toString();
        this.orderStatus = orderStatus;
        this.totalRevenue = totalRevenue;
    }
}
