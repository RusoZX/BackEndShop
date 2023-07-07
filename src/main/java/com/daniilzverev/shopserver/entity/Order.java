package com.daniilzverev.shopserver.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name= "`order`")
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    @ManyToOne(optional = true)
    @JoinColumn(name="userAddress")
    private Address address;

    @Column(name="paymentMethod")
    private String paymentMethod;

    @Column(name="deliveryMethod")
    private String deliveryMethod;

    @Column(name="paymentStatus")
    private Boolean paymentStatus;

    @Column(name="orderStatus")
    private String orderStatus;

    @CreationTimestamp
    @Column(name = "createdDate")
    private LocalDateTime createdDate;


    public String toJson(){
        return "{\"id\":"+this.id+",\"userId\":\""+this.user.getId()+"\",\"paymentMethod\":\""+this.paymentMethod+
                "\",\"deliveryMethod\":\""+this.deliveryMethod+"\",\"paymentStatus\":\""+this.paymentStatus+
                "\",\"orderStatus\":\""+this.orderStatus+
                "\"}";
    }

}
