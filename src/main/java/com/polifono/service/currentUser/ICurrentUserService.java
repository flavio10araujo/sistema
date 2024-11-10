package com.polifono.service.currentUser;

import com.polifono.model.CurrentUser;

public interface ICurrentUserService {

    boolean canAccessUser(CurrentUser currentUser, Long userId);
}
