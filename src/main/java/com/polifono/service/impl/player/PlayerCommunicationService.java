package com.polifono.service.impl.player;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.polifono.common.util.DateUtil;
import com.polifono.model.entity.Player;
import com.polifono.repository.IPlayerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PlayerCommunicationService {

    private final IPlayerRepository repository;

    public List<Player> findCommunicationGroup04() {
        return repository.findCommunicationGroup04(DateUtil.subtractMonth(new Date(), 1), DateUtil.subtractMonth(new Date(), 2));
    }

    public List<Player> findCommunicationGroup05() {
        return repository.findCommunicationGroup05(DateUtil.subtractMonth(new Date(), 1), DateUtil.subtractMonth(new Date(), 2));
    }
}
