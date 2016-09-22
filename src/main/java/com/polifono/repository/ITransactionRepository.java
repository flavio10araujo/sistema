package com.polifono.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.polifono.domain.Transaction;

public interface ITransactionRepository extends CrudRepository<Transaction, Integer> {

	@Query("SELECT transaction FROM Transaction transaction WHERE transaction.code = :code")
	public List<Transaction> findTransactionsByCode(@Param("code") String code);
}