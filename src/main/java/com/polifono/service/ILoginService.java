package com.polifono.service;

import java.util.List;

import com.polifono.domain.Login;
import com.polifono.domain.Player;

public interface ILoginService {

	List<Login> findByPlayer(int playerId);

	Login registerLogin(Player player);
}
