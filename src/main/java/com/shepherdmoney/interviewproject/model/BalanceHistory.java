package com.shepherdmoney.interviewproject.model;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity class that represents the balance history of a credit card.
 * Each instance of this class represents a record of a credit card's balance at a specific point in time.
 */
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class BalanceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    private Instant date;

    private double balance;

    @ManyToOne
    private CreditCard creditCard;
    
    public BalanceHistory(Instant date, double balance){
        this.date = date;
        this.balance = balance;
    }
}
