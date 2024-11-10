package com.polifono.service;

import java.util.Date;

import com.polifono.model.entity.Promo;

public interface IPromoService {

    Promo save(Promo o);

    Promo findByCode(String code);

    Promo findByCodeAndDate(String code, Date dt);
}
