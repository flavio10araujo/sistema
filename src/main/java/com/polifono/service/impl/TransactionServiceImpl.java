package com.polifono.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.Player;
import com.polifono.domain.Transaction;
import com.polifono.repository.ITransactionRepository;
import com.polifono.service.ITransactionService;

@Service
public class TransactionServiceImpl implements ITransactionService {

    private final ITransactionRepository repository;

    @Autowired
    public TransactionServiceImpl(final ITransactionRepository transactionRepository) {
        this.repository = transactionRepository;
    }

    public final Transaction save(Transaction transaction) {
        return repository.save(transaction);
    }
	
    public final Optional<Transaction> findById(int transactionId) {
        return repository.findById(transactionId);
    }

    public final List<Transaction> findByCode(String code) {
        return repository.findByCode(code);
    }

    public final List<Transaction> findByPlayerAndStatus(Player player, int status) {
        return repository.findByPlayerAndStatus(player, status);
    }
}
