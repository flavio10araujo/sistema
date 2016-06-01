package com.polifono.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.polifono.domain.Transaction;
import com.polifono.repository.TransactionRepository;

@Service
public class TransactionService {

	private final TransactionRepository transactionRepository;
	
	@Autowired
	public TransactionService(final TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}
	
	public final Transaction save(Transaction transaction) {
		return transactionRepository.save(transaction);
	}
	
	public final Transaction find(int transactionId) {
		return transactionRepository.findOne(transactionId);
	}
	
	public final List<Transaction> findTransactionByCode(String code) {
		return transactionRepository.findTransactionByCode(code);
	}
}