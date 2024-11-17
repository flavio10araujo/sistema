package com.polifono.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.polifono.model.entity.Promo;
import com.polifono.repository.IPromoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PromoService {

    private final IPromoRepository repository;

    public Promo save(Promo promo) {
        return repository.save(promo);
    }

    public Promo findByCode(String code) {
        return repository.findByCode(code);
    }

    public Promo findByCodeAndDate(String code, Date dt) {
        return repository.findByCodeAndDate(code, dt);
    }
}
