package com.polifono.service;

import java.util.List;
import java.util.Optional;

import com.polifono.domain.Game;
import com.polifono.domain.Map;
import com.polifono.domain.Player;
import com.polifono.domain.PlayerPhase;

public interface IMapService {

    Map save(Map o);

    boolean delete(Integer id);

    Optional<Map> findById(int id);

    List<Map> findAll();

    List<Map> findMapsByGame(int gameId);

    List<Map> findMapsByGameAndLevel(int gameId, int levelId);

    Map findByGameAndLevel(int gameId, int levelId);

    Map findByGameLevelAndOrder(int gameId, int levelId, int mapOrder);

    Map findNextMapSameLevel(Map mapCurrent);

    boolean playerCanAccessThisMap(Map map, Player user);

    Map findCurrentMap(Game game, PlayerPhase playerPhase);
}
