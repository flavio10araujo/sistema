package com.polifono.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.polifono.domain.Login;
import com.polifono.domain.Player;
import com.polifono.repository.ILoginRepository;
import com.polifono.service.ILoginService;

@Service
public class LoginServiceImpl implements ILoginService {

    private ILoginRepository repository;

    @Autowired
    public LoginServiceImpl(ILoginRepository repository) {
        this.repository = repository;
    }

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

        List<Login> list = new ArrayList<>();
        for (Date s : listDates) {
            Login l = new Login();
            l.setDtLogin(s);
            list.add(l);
        }

        if (list.isEmpty()) {
            return null;
        }

        return list;
    }
}
