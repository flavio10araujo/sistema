package com.polifono.service;

import java.util.List;
import java.util.Optional;

import com.polifono.domain.Player;
import com.polifono.domain.Transaction;

public interface ITransactionService {

    public Transaction save(Transaction o);

    public Optional<Transaction> findById(int id);

    public List<Transaction> findByCode(String code);

    public List<Transaction> findByPlayerAndStatus(Player player, int status);
}
