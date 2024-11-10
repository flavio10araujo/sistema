package com.polifono.service;

import java.util.List;
import java.util.Optional;

import com.polifono.model.entity.Player;
import com.polifono.model.entity.Transaction;

public interface ITransactionService {

    Transaction save(Transaction o);

    Optional<Transaction> findById(int id);

    List<Transaction> findByCode(String code);

    List<Transaction> findByPlayerAndStatus(Player player, int status);
}
