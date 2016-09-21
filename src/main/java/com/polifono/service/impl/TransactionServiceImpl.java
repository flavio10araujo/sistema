package com.polifono.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	public final Transaction find(int transactionId) {
		return repository.findOne(transactionId);
	}
	
	public final List<Transaction> findTransactionByCode(String code) {
		return repository.findTransactionByCode(code);
	}
}