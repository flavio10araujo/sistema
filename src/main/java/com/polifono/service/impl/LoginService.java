package com.polifono.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.polifono.model.entity.Login;
import com.polifono.model.entity.Player;
import com.polifono.repository.ILoginRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LoginService {

    private final ILoginRepository repository;

    public Login registerLogin(Player player) {
        return repository.save(prepareForRegisterLogin(player));
    }

    public Login prepareForRegisterLogin(Player player) {
        Login login = new Login();
        login.setPlayer(player);
        login.setDtLogin(new Date());
        return login;
    }

    public List<Login> findByPlayer(int playerId) {
        List<Date> listDates = repository.findByPlayer(playerId, PageRequest.of(0, 30));

        List<Login> list = listDates.stream()
                .map(date -> {
                    Login login = new Login();
                    login.setDtLogin(date);
                    return login;
                })
                .collect(Collectors.toList());

        return list.isEmpty() ? null : list;
    }
}
