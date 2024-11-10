package com.polifono.service.currentUser;

import org.springframework.stereotype.Service;

import com.polifono.model.CurrentUser;
import com.polifono.model.enums.Role;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CurrentUserServiceImpl implements ICurrentUserService {

    @Override
    public boolean canAccessUser(CurrentUser currentUser, Long userId) {
        log.debug("Checking if user={} has access to user={}", currentUser, userId);
        return currentUser != null && (currentUser.getRole() == Role.ADMIN || currentUser.getId().equals(userId));
    }
}
