package com.shepherdmoney.interviewproject.repository;

import com.shepherdmoney.interviewproject.model.CreditCard;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Crud repository to store credit cards
 */
@Repository("CreditCardRepo")
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {

    @Query(value = "SELECT * FROM MY_CREDIT_CARD WHERE USER_ID = :userId", nativeQuery = true)
    List<CreditCard> findByOwner(@Param("userId") int userId);

    @Query(value = "SELECT * FROM MY_CREDIT_CARD WHERE NUMBER = :number", nativeQuery = true)
    CreditCard findByNumber(String number);
}
