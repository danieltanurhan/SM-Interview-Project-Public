package com.shepherdmoney.interviewproject.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

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
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String issuanceBank;

    private String number;

    @ManyToOne
    private User owner;

    @OneToMany(mappedBy = "creditCard", cascade = CascadeType.ALL)
    private List<BalanceHistory> balanceHistory = new ArrayList<>();

    
    public CreditCard(String issuanceBank, String number, User owner){
        this.issuanceBank = issuanceBank;
        this.number = number;
        this.owner = owner;
    }

    public void addBalanceHistory(BalanceHistory balanceHistory) {
        this.balanceHistory.add(balanceHistory);
        sortBalanceHistory();
    }
    
    /**
     * Sorts the credit card's balance history list in descending order by date.
     */
    private void sortBalanceHistory() {
        Collections.sort(balanceHistory, (bh1, bh2) -> bh2.getDate().compareTo(bh1.getDate()));        
    }

    public List<BalanceHistory> getBalanceList(){
        return balanceHistory;
    }
}
