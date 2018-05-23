package com.polifono.service.currentUser;

import com.polifono.domain.bean.CurrentUser;

public interface ICurrentUserService {

    boolean canAccessUser(CurrentUser currentUser, Long userId);

}