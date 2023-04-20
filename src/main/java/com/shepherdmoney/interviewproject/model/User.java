package com.shepherdmoney.interviewproject.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "MyUser")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    private String email;

        @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
        private List<CreditCard> creditCards = new ArrayList<>();

        public User(String name, String email){
            this.name = name;
            this.email = email;
        }
    
        public void addCreditCard(CreditCard creditCard) {
            creditCards.add(creditCard);
            creditCard.setOwner(this);
        }
    
        public void removeCreditCard(CreditCard creditCard) {
            creditCards.remove(creditCard);
            creditCard.setOwner(null);
        }
}
