package com.polifono.service;

import java.util.List;

import com.polifono.domain.Transaction;

public interface ITransactionService {

	public Transaction save(Transaction o);
	
	//public Boolean delete(Integer id);
	
	public Transaction findOne(int id);
	
	//public List<Transaction> findAll();
	
	
	public List<Transaction> findByCode(String code);

}