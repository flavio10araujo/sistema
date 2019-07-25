package com.polifono.controller;

import java.util.ArrayList;
import java.util.List;

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
	
	@GetMapping(value="/playervideos")
	@ResponseBody
    public List<PlayervideoDTO> playervideosGeneral(@RequestParam(value = "page") String pageStr, @RequestParam(value = "size") String sizeStr) {

		try {
			int page = Integer.parseInt(pageStr);
			int size = Integer.parseInt(sizeStr);
			
			if (size > 10) {
				size = 10;
			}
			
			Sort sort = new Sort(new Sort.Order(Direction.DESC, "dtInc"));
			Pageable pageable = new PageRequest(page, size, sort);
			
			return convertToDto(playervideoService.findAll(pageable));
		}
		catch (Exception e) {
			return new ArrayList<PlayervideoDTO>();
		}
    }
	
	@GetMapping(value="/playervideos/content/{contentId}")
	@ResponseBody
    public List<PlayervideoDTO> playervideosByContent(@PathVariable("contentId") Integer contentId, @RequestParam(value = "page") String pageStr, @RequestParam(value = "size") String sizeStr) {

		Content content = contentService.findOne(contentId);
		
		if (content == null) return new ArrayList<PlayervideoDTO>();
		
		try {
			int page = Integer.parseInt(pageStr);
			int size = Integer.parseInt(sizeStr);
			
			if (size > 10) {
				size = 10;
			}
			
			Sort sort = new Sort(new Sort.Order(Direction.DESC, "dtInc"));
			Pageable pageable = new PageRequest(page, size, sort);
			
			return convertToDto(playervideoService.findAllByContent(content, pageable));
		}
		catch (Exception e) {
			return new ArrayList<PlayervideoDTO>();
		}
    }
	
	@GetMapping(value="/playervideos/player/{playerId}")
	@ResponseBody
    public List<PlayervideoDTO> playervideosByPlayer(@PathVariable("playerId") Integer playerId, @RequestParam(value = "page") String pageStr, @RequestParam(value = "size") String sizeStr) {

		Player player = playerService.findOne(playerId);
		
		if (player == null) return new ArrayList<PlayervideoDTO>();
		
		try {
			int page = Integer.parseInt(pageStr);
			int size = Integer.parseInt(sizeStr);
			
			if (size > 10) {
				size = 10;
			}
			
			Sort sort = new Sort(new Sort.Order(Direction.DESC, "dtInc"));
			Pageable pageable = new PageRequest(page, size, sort);
			
			return convertToDto(playervideoService.findAllByPlayer(player, pageable));
		}
		catch (Exception e) {
			return new ArrayList<PlayervideoDTO>();
		}
    }
	
	private List<PlayervideoDTO> convertToDto(List<Playervideo> playervideoList) {
		List<PlayervideoDTO> dtoList = new ArrayList<PlayervideoDTO>();
		
		for (Playervideo playervideo : playervideoList) {
			PlayervideoDTO dto = new PlayervideoDTO();
			
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