package com.polifono.service;

import java.util.List;

import com.polifono.domain.Transaction;

public interface ITransactionService {

	public Transaction save(Transaction transaction);
	
	public Transaction find(int transactionId);
	
	public List<Transaction> findTransactionByCode(String code);

}