package com.daniilzverev.shopserver.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

//This annotation will create all the getters/setters needed
@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name="user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="surname")
    private String surname;

    @Column(name="birthdate")
    private LocalDate birthDate;

    @Column(name="email")
    private String email;

    @Column(name="pwd")
    private String pwd;

    @Column(name="role")
    private String role;

    @ManyToMany(mappedBy = "shoppingCart")
    private Set<Product> shoppingCart = new HashSet<>();

}
