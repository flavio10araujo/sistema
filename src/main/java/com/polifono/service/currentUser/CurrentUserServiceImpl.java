package com.polifono.service.currentUser;

import org.springframework.stereotype.Service;

import com.polifono.model.CurrentUser;
import com.polifono.model.enums.Role;

@Service
public class CurrentUserServiceImpl implements ICurrentUserService {

    @Override
    public boolean canAccessUser(CurrentUser currentUser, Long userId) {
        return currentUser != null && (currentUser.getRole() == Role.ADMIN || currentUser.getId().equals(userId));
    }
}
