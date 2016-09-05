package com.polifono.service.currentUser;

import com.polifono.domain.CurrentUser;

public interface ICurrentUserService {

    boolean canAccessUser(CurrentUser currentUser, Long userId);

}