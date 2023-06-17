package com.daniilzverev.shopserver.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name="order")
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

    @Column(name="payment")
    private String payment;

    @Column(name="deliveryMethod")
    private String deliveryMethod;

    @ManyToMany(mappedBy = "goods")
    private Set<Product> goods = new HashSet<>();
}
