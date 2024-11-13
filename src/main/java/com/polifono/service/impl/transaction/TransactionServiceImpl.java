package com.polifono.service.impl.transaction;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.polifono.model.entity.Player;
import com.polifono.model.entity.Transaction;
import com.polifono.repository.ITransactionRepository;
import com.polifono.service.ITransactionService;
import com.polifono.service.impl.SendEmailService;
import com.polifono.service.impl.player.PlayerCreditService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements ITransactionService {

    private final ITransactionRepository repository;

    @Override
    public Transaction save(Transaction transaction) {
        return repository.save(transaction);
    }

    @Override
    public Optional<Transaction> findById(int transactionId) {
        return repository.findById(transactionId);
    }

    @Override
    public List<Transaction> findByCode(String code) {
        return repository.findByCode(code);
    }

    @Override
    public List<Transaction> findByPlayerAndStatus(Player player, int status) {
        return repository.findByPlayerAndStatus(player, status);
    }
}
