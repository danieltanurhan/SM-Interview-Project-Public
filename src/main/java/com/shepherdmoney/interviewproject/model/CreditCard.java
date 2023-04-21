package com.shepherdmoney.interviewproject.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.*;

/**
 * The CreditCard entity represents a user's credit card.
 * This class is annotated with @Entity indicating that it is a JPA entity.
 * 
 * It also includes the @Getter, @Setter, @ToString and @RequiredArgsConstructor
 * annotations for convenience.
 */
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "My_CreditCard")
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String issuanceBank;

    private String number;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    
    @OneToMany(mappedBy = "creditCard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BalanceHistory> balanceHistory = new ArrayList<>();

    public CreditCard(String issuanceBank, String number, User owner){
        this.issuanceBank = issuanceBank;
        this.number = number;
        this.owner = owner;
    }
}
