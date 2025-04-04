package com.polifono.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.polifono.model.entity.Player;
import com.polifono.model.entity.Transaction;

public interface ITransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findByCode(String code);

    List<Transaction> findByPlayerAndStatus(Player player, int status);
}
