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
@Table(name="product")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="title")
    private String title;

    @Column(name="price")
    private Float price;

    @Column(name="category")
    private String category;

    @Column(name="brand")
    private String brand;

    @Column(name="color")
    private String color;

    @Column(name="weight")
    private Float weight;

    @Column(name="volume")
    private Float volume;

    @Column(name="stock")
    private Integer stock;

    @Column(name="totalSold")
    private Integer totalSold;

    private byte[] imageData;
    private String type;

}
