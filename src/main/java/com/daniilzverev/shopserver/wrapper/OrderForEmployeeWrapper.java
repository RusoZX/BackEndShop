package com.daniilzverev.shopserver.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
public class OrderForEmployeeWrapper {

    private Long id;

    private String userName;

    private String paymentStatus;

    private String orderStatus;

    private LocalDate createdDate;

    private Double totalRevenue;

    public OrderForEmployeeWrapper(Long id,String userName, Boolean paymentStatus, String orderStatus, LocalDate createdDate, Double totalRevenue) {
        this.id = id;
        this.userName =userName;
        this.paymentStatus = paymentStatus.toString();
        this.orderStatus = orderStatus;
        this.createdDate = createdDate;
        this.totalRevenue = totalRevenue;
    }
}
