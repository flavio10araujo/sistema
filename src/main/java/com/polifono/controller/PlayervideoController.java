package com.polifono.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.polifono.domain.Content;
import com.polifono.domain.Player;
import com.polifono.domain.Playervideo;
import com.polifono.dto.PlayervideoDTO;
import com.polifono.service.IContentService;
import com.polifono.service.IPlayerService;
import com.polifono.service.IPlayervideoService;
import com.polifono.util.StringUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PlayervideoController extends BaseController {

    private final IPlayervideoService playervideoService;
    private final IContentService contentService;
    private final IPlayerService playerService;

    @GetMapping(value = "/playervideos", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<PlayervideoDTO> playerVideosGeneral(@RequestParam(value = "restriction") String restriction) {

        try {
            // codigo provisorio

            if (restriction == null || restriction.isEmpty()) {
                restriction = "0";
            }

            List<PlayervideoDTO> playervideoDTOList = convertToDto(playervideoService.findRandomWithRestriction(restriction));

            // adicionar IDs na lista de restriction
            List<PlayervideoDTO> playerVideoDTOReturn = new ArrayList<>();

            label:
            {
                for (PlayervideoDTO p : playervideoDTOList) {
                    if (verifyRestriction(restriction, p.id())) {
                        playerVideoDTOReturn.add(p);
                    }

                    if (playerVideoDTOReturn.size() >= 3) {
                        break label;
                    }
                }
            }

            return playerVideoDTOReturn;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public boolean verifyRestriction(String restriction, int id) {

        if (restriction == null || restriction.isEmpty() || "0".equals(restriction)) {
            return true;
        }

        String[] restrictionArr = restriction.split(",");

        for (String s : restrictionArr) {
            if (s.equals(id + "")) {
                return false;
            }
        }

        return true;
    }

    @GetMapping(value = "/playervideos/content/{contentId}", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<PlayervideoDTO> playerVideosByContent(@PathVariable("contentId") Integer contentId, @RequestParam(value = "page") String pageStr,
            @RequestParam(value = "size") String sizeStr) {

        Optional<Content> content = contentService.findById(contentId);

        if (content.isEmpty())
            return new ArrayList<>();

        try {
            int page = Integer.parseInt(pageStr);
            int size = Integer.parseInt(sizeStr);

            if (size > 10) {
                size = 10;
            }

            List<Sort.Order> orders = new ArrayList<>();
            orders.add(new Sort.Order(Direction.DESC, "dtInc"));
            Sort sort = Sort.by(orders);

            Pageable pageable = PageRequest.of(page, size, sort);

            return convertToDto(playervideoService.findAllByContent(content.get(), pageable));
        } catch (Exception e) {
            return new ArrayList<PlayervideoDTO>();
        }
    }

    @GetMapping(value = "/playervideos/player/{playerId}", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<PlayervideoDTO> playerVideosByPlayer(@PathVariable("playerId") Integer playerId, @RequestParam(value = "page") String pageStr,
            @RequestParam(value = "size") String sizeStr) {

        Optional<Player> playerOpt = playerService.findById(playerId);

        if (playerOpt.isEmpty())
            return new ArrayList<>();

        Player player = playerOpt.get();

        try {
            int page = Integer.parseInt(pageStr);
            int size = Integer.parseInt(sizeStr);

            if (size > 10) {
                size = 10;
            }

            List<Sort.Order> orders = new ArrayList<>();
            orders.add(new Sort.Order(Direction.DESC, "dtInc"));
            Sort sort = Sort.by(orders);

            Pageable pageable = PageRequest.of(page, size, sort);

            return convertToDto(playervideoService.findAllByPlayer(player, pageable));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private List<PlayervideoDTO> convertToDto(List<Playervideo> playervideoList) {
        List<PlayervideoDTO> dtoList = new ArrayList<>();

        for (Playervideo playervideo : playervideoList) {
            dtoList.add(new PlayervideoDTO(
                    playervideo.getId(),
                    playervideo.getContent().getPhase().getMap().getGame().getName(),
                    playervideo.getContent().getPhase().getMap().getLevel().getName(),
                    playervideo.getContent().getPhase().getOrder(),
                    playervideo.getPlayer().getId(),
                    playervideo.getPlayer().getName(),
                    playervideo.getPlayer().getLastName(),
                    StringUtil.formatYoutubeUrl(playervideo.getUrl())));
        }

        return dtoList;
    }
}
