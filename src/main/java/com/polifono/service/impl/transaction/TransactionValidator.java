package com.polifono.service.impl.transaction;

import java.util.List;

import org.springframework.stereotype.Service;

import com.polifono.model.entity.Transaction;

@Service
public class TransactionValidator {

    public boolean isTransactionAlreadyRegistered(List<Transaction> transactions) {
        return transactions != null && !transactions.isEmpty();
    }
}
