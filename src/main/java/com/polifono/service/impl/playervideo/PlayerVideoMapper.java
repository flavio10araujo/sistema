package com.polifono.service.impl.playervideo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.polifono.common.util.YouTubeUrlFormatter;
import com.polifono.model.dto.PlayervideoDTO;
import com.polifono.model.entity.Playervideo;

@Service
public class PlayerVideoMapper {

    public List<PlayervideoDTO> toDtoList(List<Playervideo> playervideoList) {
        return playervideoList.stream().map(playervideo -> new PlayervideoDTO(
                playervideo.getId(),
                playervideo.getContent().getPhase().getMap().getGame().getName(),
                playervideo.getContent().getPhase().getMap().getLevel().getName(),
                playervideo.getContent().getPhase().getOrder(),
                playervideo.getPlayer().getId(),
                playervideo.getPlayer().getName(),
                playervideo.getPlayer().getLastName(),
                YouTubeUrlFormatter.formatUrl(playervideo.getUrl())
        )).collect(Collectors.toList());
    }
}
