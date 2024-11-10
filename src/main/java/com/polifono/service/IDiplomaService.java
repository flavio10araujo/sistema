package com.polifono.service;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.polifono.model.entity.Diploma;
import com.polifono.model.entity.Phase;
import com.polifono.model.entity.Player;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;

public interface IDiplomaService {

    Diploma save(Diploma o);

    Optional<Diploma> findByCode(String code);

    List<Diploma> findByPlayer(int playerId);

    void generateDiplomaPdf(HttpServletResponse response, Diploma diploma, Locale locale) throws JRException, IOException;

    Diploma setupDiploma(Player player, Phase currentPhase);
}
