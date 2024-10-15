package com.polifono.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.polifono.domain.Promo;
import com.polifono.repository.IPromoRepository;
import com.polifono.service.IPromoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PromoServiceImpl implements IPromoService {

    private final IPromoRepository repository;

    public Promo save(Promo promo) {
        return repository.save(promo);
    }

    @Override
    public Promo findByCode(String code) {
        return repository.findByCode(code);
    }

    @Override
    public Promo findByCodeAndDate(String code, Date dt) {
        return repository.findByCodeAndDate(code, dt);
    }
}
