package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.model.BalanceHistory;
import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.BalanceHistoryRepository;
import com.shepherdmoney.interviewproject.repository.CreditCardRepository;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import com.shepherdmoney.interviewproject.vo.request.AddCreditCardToUserPayload;
import com.shepherdmoney.interviewproject.vo.request.UpdateBalancePayload;
import com.shepherdmoney.interviewproject.vo.response.CreditCardView;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * The CreditCardController class handles incoming requests related to credit card operations.
 * 
 * This class is annotated with @RestController. It uses the CreditCardRepository and UserRepository interfaces
 * to interact with the database and retrieve credit card and user information.
 * 
 * The CreditCardController class contains endpoints for managing credit cards.
 * Endpoints:
 * POST /credit-card: Add a credit card to a user
 * GET /credit-card:all: Retrieve all credit cards associated with a user
 * GET /credit-card:user-id: Retrieve the user ID associated with a given credit card number
 * POST /credit-card:update-balance: Update the balance history of a credit card
 * GET /credit-card:balance-history: Retrieve a list of balance history for a given credit card number
 */
@RestController
public class CreditCardController {

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BalanceHistoryRepository balanceHistoryRepository;

    /**
     * Adds a credit card to the user with the specified user ID.
     *
     * @param payload the payload containing the user ID, card issuance bank, and card number
     * @return a ResponseEntity with the HTTP status code and the ID of the newly created credit card
     */
    @PostMapping("/credit-card")
    @Transactional
    public ResponseEntity<Integer> addCreditCardToUser(@RequestBody AddCreditCardToUserPayload payload) {
        int userId = payload.getUserId();
        
        // Check if the userId exists
        if(!userRepository.existsById(userId)) {
            return ResponseEntity.badRequest().build();
        }

        User user = userRepository.getReferenceById(userId);
        CreditCard creditCard = new CreditCard(payload.getCardIssuanceBank(), payload.getCardNumber(), user);

        // Save the credit card to the database and return its ID
        creditCardRepository.save(creditCard);
        return ResponseEntity.ok(creditCard.getId());
    }

    /**
     * Retrieves all credit cards associated with a given user ID.
     * 
     * @param userId the ID of the user to retrieve credit cards for
     * @return a list of {@link CreditCardView} objects representing the credit cards
     * @throws BadRequestException if the user with the given ID does not exist
     */
    @GetMapping("/credit-card:all")
    public ResponseEntity<List<CreditCardView>> getAllCardOfUser(@RequestParam int userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()) {
            List<CreditCard> creditCards = creditCardRepository.findByOwner(userId);
        
        //Convert CreditCard object to CreditCardView objects
        List<CreditCardView> creditCardViews = creditCards.stream()
                .map(creditCard -> new CreditCardView(creditCard.getIssuanceBank(), creditCard.getNumber()))
                .collect(Collectors.toList());

                return ResponseEntity.ok(creditCardViews);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
    * Returns the user ID associated with the given credit card number.
    * 
    * @param creditCardNumber the number of the credit card to check
    * @return a ResponseEntity with the user ID if the credit card is found, or a bad request status if it is not found
    */
    @GetMapping("/credit-card:user-id")
    public ResponseEntity<Integer> getUserIdForCreditCard(@RequestParam String creditCardNumber) {
        CreditCard creditCard = creditCardRepository.findByNumber(creditCardNumber);
        if(creditCard == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(creditCard.getOwner().getId());
    }

    /**
     * Update the balance history of a credit card.
     *
     * @param payload An array of UpdateBalancePayload objects containing information about each transaction.
     * @return A ResponseEntity with a message indicating whether the balance was updated successfully or not.
     */
    @PostMapping("/credit-card:update-balance")
    @Transactional
    public ResponseEntity<String> updateBalance(@RequestBody UpdateBalancePayload[] payload) {
        for(UpdateBalancePayload transaction : payload) {
            // Find the credit card based on the card number
            CreditCard creditCard = creditCardRepository.findByNumber(transaction.getCreditCardNumber());
            if(creditCard == null) {
                return ResponseEntity.badRequest().body("Invalid card number");
            }
            
            // Create a new balance history object for the transaction
            BalanceHistory balance = new BalanceHistory(transaction.getTransactionTime(), transaction.getCurrentBalance(), creditCard);
            
            balanceHistoryRepository.save(balance);
        }
        return ResponseEntity.ok("Balance updated successfully.");
    }

    /**
     * Returns a list of balance history for a given credit card number.
     * 
     * @param number the credit card number
     * @return a list of balance history, represented as a list of maps containing the date and balance.
     * If the credit card number is invalid, returns a bad request response.
     */
    @GetMapping("/credit-card:balance-history")
    public ResponseEntity<List<Map<String, String>>> getBalanceHistory(@RequestParam String number){
        CreditCard creditCard = creditCardRepository.findByNumber(number);
        if(creditCard == null) {
            return ResponseEntity.badRequest().build();
        }

        //Get List of balance history from repository
        List<BalanceHistory> list = balanceHistoryRepository.findByCreditCard(creditCard.getId());
        
        // Map list of BalanceHistory objects to maps containing date and balance 
        List<Map<String, String>> balanceHistoryList = list.stream()
            .map(balanceHistory -> {
                Map<String, String> map = new HashMap<>();
                map.put("date", balanceHistory.getDate().toString().substring(0, 10));
                map.put("balance", ""+ balanceHistory.getBalance());
                return map;
            })
            .collect(Collectors.toList());

        //Sort from newest to oldest date    
        Collections.sort(balanceHistoryList, (bh1, bh2) -> bh2.get("date").compareTo(bh1.get("date")));
        
        return ResponseEntity.ok(balanceHistoryList);
    }
    
}
