package com.polifono.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.polifono.domain.Promo;

public interface IPromoRepository extends JpaRepository<Promo, Integer> {

    Promo findByCode(String code);

    @Query("SELECT promo FROM Promo promo WHERE promo.code = :code AND (promo.dtBegin <= :dtToday AND :dtToday <= promo.dtEnd)")
    Promo findByCodeAndDate(@Param("code") String code, @Param("dtToday") Date dtToday);
}
