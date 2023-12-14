package com.polifono.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class PlayervideoController extends BaseController {

    @Autowired
    private IPlayervideoService playervideoService;

    @Autowired
    private IContentService contentService;

    @Autowired
    private IPlayerService playerService;

    @GetMapping(value = "/playervideos")
    @ResponseBody
    public List<PlayervideoDTO> playervideosGeneral(@RequestParam(value = "restriction") String restriction) {

        try {
            // codigo provisorio

            if (restriction == null || "".equals(restriction)) {
                restriction = "0";
            }

            List<PlayervideoDTO> playervideoDTOList = convertToDto(playervideoService.findRandomWithRestriction(restriction));

            // adicionar IDs na lista de restriction
            List<PlayervideoDTO> playervideoDTOReturn = new ArrayList<>();

            label:
            {
                for (PlayervideoDTO p : playervideoDTOList) {
                    if (verifyRestriction(restriction, p.getId())) {
                        playervideoDTOReturn.add(p);
                    }

                    if (playervideoDTOReturn.size() >= 3) {
                        break label;
                    }
                }
            }

            return playervideoDTOReturn;
        } catch (Exception e) {
            return new ArrayList<PlayervideoDTO>();
        }
    }

    public boolean verifyRestriction(String restriction, int id) {

        if (restriction == null || "".equals(restriction) || "0".equals(restriction)) {
            return true;
        }

        String[] restrictionArr = restriction.split(",");

        for (int i = 0; i < restrictionArr.length; i++) {
            if (restrictionArr[i].equals(id + "")) {
                return false;
            }
        }

        return true;
    }

    @GetMapping(value = "/playervideos/content/{contentId}")
    @ResponseBody
    public List<PlayervideoDTO> playervideosByContent(@PathVariable("contentId") Integer contentId, @RequestParam(value = "page") String pageStr,
            @RequestParam(value = "size") String sizeStr) {

        Optional<Content> content = contentService.findById(contentId);

        if (!content.isPresent())
            return new ArrayList<PlayervideoDTO>();

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

    @GetMapping(value = "/playervideos/player/{playerId}")
    @ResponseBody
    public List<PlayervideoDTO> playervideosByPlayer(@PathVariable("playerId") Integer playerId, @RequestParam(value = "page") String pageStr,
            @RequestParam(value = "size") String sizeStr) {

        Optional<Player> playerOpt = playerService.findById(playerId);

        if (!playerOpt.isPresent())
            return new ArrayList<PlayervideoDTO>();

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
            return new ArrayList<PlayervideoDTO>();
        }
    }

    private List<PlayervideoDTO> convertToDto(List<Playervideo> playervideoList) {
        List<PlayervideoDTO> dtoList = new ArrayList<PlayervideoDTO>();

        for (Playervideo playervideo : playervideoList) {
            PlayervideoDTO dto = new PlayervideoDTO();

            dto.setId(playervideo.getId());
            dto.setGameName(playervideo.getContent().getPhase().getMap().getGame().getName());
            dto.setLevelName(playervideo.getContent().getPhase().getMap().getLevel().getName());
            dto.setPhaseOrder(playervideo.getContent().getPhase().getOrder());
            dto.setPlayerId(playervideo.getPlayer().getId());
            dto.setPlayerFirstName(playervideo.getPlayer().getName());
            dto.setPlayerLastName(playervideo.getPlayer().getLastName());
            dto.setUrl(StringUtil.formatYoutubeUrl(playervideo.getUrl()));

            dtoList.add(dto);
        }

        return dtoList;
    }
}
