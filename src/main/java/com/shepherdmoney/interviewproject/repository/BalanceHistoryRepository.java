package com.shepherdmoney.interviewproject.repository;

import com.shepherdmoney.interviewproject.model.BalanceHistory;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Crud repository to store credit cards
 */
@Repository("BalanceHistoryRepo")
public interface BalanceHistoryRepository extends JpaRepository<BalanceHistory, Integer> {

    @Query(value = "SELECT * FROM BALANCE_HISTORY WHERE CREDIT_CARD_ID = :creditCardId", nativeQuery = true)
    List<BalanceHistory> findByCreditCard(int creditCardId);

}