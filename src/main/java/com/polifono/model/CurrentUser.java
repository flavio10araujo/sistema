package com.polifono.model;

import org.springframework.security.core.authority.AuthorityUtils;

import com.polifono.model.entity.Player;
import com.polifono.model.enums.Role;

import lombok.Getter;

@Getter
public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private final Player user;

    public CurrentUser(Player user) {
        super(
                (user.getEmail() == null ? user.getLogin() : user.getEmail()),
                user.getPassword(),
                AuthorityUtils.createAuthorityList(user.getRole().toString())
        );

        this.user = user;
    }

    public Long getId() {
        return (long) user.getId();
    }

    public Role getRole() {
        return user.getRole();
    }

    @Override
    public String toString() {
        return "CurrentUser{" +
                "user=" + user +
                "} " + super.toString();
    }
}
