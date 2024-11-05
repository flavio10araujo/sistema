package com.polifono.service.helper;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class GameHelperService {

    public boolean isValidLevelOrder(Integer levelOrder) {
        return levelOrder >= 1 && levelOrder <= 5;
    }
}
