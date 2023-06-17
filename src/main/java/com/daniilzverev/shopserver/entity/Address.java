package com.daniilzverev.shopserver.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name="address")
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="country")
    private String country;

    @Column(name="city")
    private String city;

    @Column(name="postalCode")
    private String postalCode;

    @Column(name="street")
    private String street;

    @Column(name="home")
    private String home;

    @Column(name="apartment")
    private String apartment;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;
}
