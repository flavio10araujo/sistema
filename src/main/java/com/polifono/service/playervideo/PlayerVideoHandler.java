package com.polifono.service.playervideo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.polifono.model.dto.PlayervideoDTO;

@Service
public class PlayerVideoHandler {

    public List<PlayervideoDTO> filterAndLimit(List<PlayervideoDTO> playervideoDTOList, String restriction) {
        return playervideoDTOList.stream()
                .filter(video -> verifyRestriction(restriction, video.id()))
                .limit(3)
                .collect(Collectors.toList());
    }

    private boolean verifyRestriction(String restriction, int id) {
        if ("0".equals(restriction)) {
            return true;
        }

        return Arrays.stream(restriction.split(",")).noneMatch(r -> r.equals(String.valueOf(id)));
    }
}
