package com.polifono.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.polifono.domain.Diploma;

public interface IDiplomaRepository extends JpaRepository<Diploma, Integer> {

    @Query("SELECT diploma FROM Diploma diploma WHERE diploma.player.id = :playerId ORDER BY diploma.dt DESC")
    List<Diploma> findByPlayer(@Param("playerId") int playerId);

    Optional<Diploma> findByCode(String code);
}
