package com.polifono.service;

import java.util.Date;

import com.polifono.domain.Promo;

public interface IPromoService {

	public Promo save(Promo o);
	
	//public Boolean delete(Integer id);
	
	//public Diploma findOne(int id);
	
	//public List<Diploma> findAll();
	
	
	public Promo findByCode(String code);
	
	public Promo findByCodeAndDate(String code, Date dt);
}