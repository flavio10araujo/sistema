package com.polifono.controller.admin.basic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.polifono.controller.BaseController;
import com.polifono.domain.Map;
import com.polifono.domain.Phase;
import com.polifono.domain.Question;
import com.polifono.domain.bean.Combo;
import com.polifono.service.IMapService;
import com.polifono.service.IPhaseService;
import com.polifono.service.IQuestionService;

@RestController
public class ComboController extends BaseController {

    @Autowired
    private IMapService mapService;
    
    @Autowired
    private IPhaseService phaseService;
    
    @Autowired
    private IQuestionService questionService;

    @RequestMapping("/comboMap")
    public List<Combo> comboMap(@RequestParam(value="gameId") String gameId, @RequestParam(value="levelId") String levelId) {
        List<Combo> list = new ArrayList<Combo>();
    	
    	List<Map> auxiliar = mapService.findMapsByGameAndLevel(Integer.parseInt(gameId), Integer.parseInt(levelId));
    	
    	for (Map item : auxiliar) {
    		Combo combo = new Combo();
    		combo.setId(item.getId());
    		combo.setName(item.getOrder() + ". " + item.getName());
    		list.add(combo);
    	}
    	
    	return list;
    }
    
    @RequestMapping("/comboPhase")
    public List<Combo> comboPhase(@RequestParam(value="mapId") String mapId) {
    	List<Combo> list = new ArrayList<Combo>();
    	
    	List<Phase> auxiliar = phaseService.findByMap(Integer.parseInt(mapId));
    	
    	for (Phase item : auxiliar) {
    		Combo combo = new Combo();
    		combo.setId(item.getId());
    		combo.setName(item.getOrder() + ". " + item.getName());
    		list.add(combo);
    	}
    	
    	return list;
    }
    
    /**
     * In this method, we consider that mapId is always the first map of the list.
     * 
     * @param gameId
     * @param levelId
     * @return
     */
    @RequestMapping("/comboPhaseWithMap")
    public List<Combo> comboPhase(@RequestParam(value="gameId") String gameId, @RequestParam(value="levelId") String levelId) {
    	List<Combo> list = new ArrayList<Combo>();
    	
    	List<Map> auxiliarMap = mapService.findMapsByGameAndLevel(Integer.parseInt(gameId), Integer.parseInt(levelId));
    	
    	if (auxiliarMap != null && auxiliarMap.size() > 0) {
    	
	    	List<Phase> auxiliar = phaseService.findByMap(auxiliarMap.get(0).getId());
	    	
	    	for (Phase item : auxiliar) {
	    		Combo combo = new Combo();
	    		combo.setId(item.getId());
	    		combo.setName(item.getOrder() + ". " + item.getName());
	    		list.add(combo);
	    	}
    	
    	}
    	
    	return list;
    }
    
    @RequestMapping("/comboQuestion")
    public List<Combo> comboQuestion(@RequestParam(value="phaseId") String phaseId) {
    	List<Combo> list = new ArrayList<Combo>();
    	
    	List<Question> auxiliar = questionService.findByPhase(Integer.parseInt(phaseId));
    	
    	for (Question item : auxiliar) {
    		Combo combo = new Combo();
    		combo.setId(item.getId());
    		combo.setName(item.getOrder() + ". " + item.getName());
    		list.add(combo);
    	}
    	
    	return list;
    }
}