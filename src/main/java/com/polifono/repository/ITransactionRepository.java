package com.polifono.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.polifono.domain.Transaction;

public interface ITransactionRepository extends JpaRepository<Transaction, Integer> {

	public List<Transaction> findByCode(String code);
}