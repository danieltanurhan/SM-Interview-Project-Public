package com.shepherdmoney.interviewproject.model;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//import org.springframework.data.relational.core.mapping.Table;

/**
 * Entity class that represents the balance history of a credit card.
 * Each instance of this class represents a record of a credit card's balance at a specific point in time.
 */
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "My_Balance_History")
public class BalanceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    private Instant date;

    private double balance;

    @ManyToOne
    @JoinColumn(name = "credit_card_id")
    private CreditCard creditCard;
    
    public BalanceHistory(Instant date, double balance, CreditCard creditCard){
        this.date = date;
        this.balance = balance;
        this.creditCard = creditCard;
    }
}
