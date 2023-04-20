package com.shepherdmoney.interviewproject.repository;

import com.shepherdmoney.interviewproject.model.BalanceHistory;
import com.shepherdmoney.interviewproject.model.CreditCard;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Crud repository to store credit cards
 */
@Repository("CreditCardRepo")
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {
    @Query("SELECT c FROM CreditCard c WHERE c.number = ?1")
    CreditCard findByNumber(String number);

    // @Modifying
    // @Query("UPDATE CreditCard c SET c.balanceHistory = :balanceHistory WHERE c.id = ?:creditCardId")
    // void addBalanceHistory(@Param("creditCardId") int creditCardId, @Param("balanceHistory") List<BalanceHistory> balanceHistory);
}
