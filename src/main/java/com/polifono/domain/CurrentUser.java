package com.polifono.domain;

import org.springframework.security.core.authority.AuthorityUtils;

@SuppressWarnings("serial")
public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private Player user;

    public CurrentUser(Player user) {
        super(
        	(user.getEmail() == null ? user.getLogin() : user.getEmail()), 
        	user.getPassword(), 
        	AuthorityUtils.createAuthorityList(user.getRole().toString())
        );
    	this.user = user;
    }

    public Player getUser() {
        return user;
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