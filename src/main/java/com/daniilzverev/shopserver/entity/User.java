package com.daniilzverev.shopserver.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
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

    @ManyToMany
    @JoinTable(
            name = "shoppingCart",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "productId")
    )
    private Set<Product> shoppingCart = new HashSet<>();

    @Override
    public boolean equals(Object toCompare){
        if(toCompare instanceof User){
            User user =(User) toCompare;

            return this.id.equals(user.id) &&
                    this.name.equals(user.name) &&
                    this.surname.equals(user.surname) &&
                    this.birthDate.equals(user.birthDate) &&
                    this.email.equals(user.email) &&
                    this.pwd.equals(user.pwd) &&
                    this.role.equals(user.role);
        }
        return false;
    }

}
