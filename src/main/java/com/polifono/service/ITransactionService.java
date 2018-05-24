package com.polifono.service;

import java.util.List;

import com.polifono.domain.Transaction;

public interface ITransactionService {

	public Transaction save(Transaction transaction);
	
	public Transaction findOne(int transactionId);
	
	public List<Transaction> findByCode(String code);

}