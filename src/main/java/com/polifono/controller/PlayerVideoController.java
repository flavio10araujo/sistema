package com.polifono.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.polifono.common.util.PagingUtil;
import com.polifono.model.dto.PlayervideoDTO;
import com.polifono.service.impl.ContentService;
import com.polifono.service.impl.player.PlayerService;
import com.polifono.service.impl.playervideo.PlayerVideoHandler;
import com.polifono.service.impl.playervideo.PlayerVideoMapper;
import com.polifono.service.impl.playervideo.PlayerVideoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PlayerVideoController {

    private final PlayerVideoService playerVideoService;
    private final ContentService contentService;
    private final PlayerService playerService;
    private final PlayerVideoMapper playerVideoMapper;
    private final PlayerVideoHandler playerVideoHandler;

    @GetMapping(value = "/player_videos", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<PlayervideoDTO> playerVideosGeneral(@RequestParam(value = "restriction") String restrictionParam) {
        String restriction = Optional.ofNullable(restrictionParam).orElse("0");

        return handleException(() -> {
            List<PlayervideoDTO> playervideoDTOList = playerVideoMapper.toDtoList(playerVideoService.findRandomWithRestriction(restriction));
            return playerVideoHandler.filterAndLimit(playervideoDTOList, restriction);
        });
    }

    @GetMapping(value = "/player_videos/content/{contentId}", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<PlayervideoDTO> playerVideosByContent(
            @PathVariable("contentId") Integer contentId,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size) {

        return contentService.findById(contentId)
                .map(content -> handleException(() -> {
                    Pageable pageable = PagingUtil.createPageable(page, PagingUtil.getMaxPageSize(size), "dtInc");
                    return playerVideoMapper.toDtoList(playerVideoService.findAllByContent(content, pageable));
                }))
                .orElseGet(ArrayList::new);
    }

    @GetMapping(value = "/player_videos/player/{playerId}", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<PlayervideoDTO> playerVideosByPlayer(
            @PathVariable("playerId") Integer playerId,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size) {

        return playerService.findById(playerId)
                .map(player -> handleException(() -> {
                    Pageable pageable = PagingUtil.createPageable(page, PagingUtil.getMaxPageSize(size), "dtInc");
                    return playerVideoMapper.toDtoList(playerVideoService.findAllByPlayer(player, pageable));
                }))
                .orElseGet(ArrayList::new);
    }

    private List<PlayervideoDTO> handleException(Supplier<List<PlayervideoDTO>> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
