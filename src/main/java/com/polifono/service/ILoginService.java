package com.polifono.service;

import java.util.List;

import com.polifono.model.entity.Login;
import com.polifono.model.entity.Player;

public interface ILoginService {

    List<Login> findByPlayer(int playerId);

    Login registerLogin(Player player);
}
