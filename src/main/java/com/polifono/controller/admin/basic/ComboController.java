package com.polifono.controller.admin.basic;

import java.util.ArrayList;
import java.util.List;

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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ComboController extends BaseController {

    private final IMapService mapService;
    private final IPhaseService phaseService;
    private final IQuestionService questionService;

    @RequestMapping("/comboMap")
    public List<Combo> comboMap(@RequestParam(value = "gameId") String gameId, @RequestParam(value = "levelId") String levelId) {
        List<Combo> list = new ArrayList<>();

        List<Map> auxiliar = mapService.findMapsByGameAndLevel(Integer.parseInt(gameId), Integer.parseInt(levelId));

        for (Map item : auxiliar) {
            Combo combo = new Combo(item.getId(), item.getOrder() + ". " + item.getName());
            list.add(combo);
        }

        return list;
    }

    @RequestMapping(value = "/comboPhase", produces = "application/json; charset=UTF-8")
    public List<Combo> comboPhase(@RequestParam(value = "mapId") String mapId) {
        List<Combo> list = new ArrayList<>();

        List<Phase> auxiliar = phaseService.findByMap(Integer.parseInt(mapId));

        for (Phase item : auxiliar) {
            Combo combo = new Combo(item.getId(), item.getOrder() + ". " + item.getName());
            list.add(combo);
        }

        return list;
    }

    /**
     * In this method, we consider that mapId is always the first map of the list.
     */
    @RequestMapping(value = "/comboPhaseWithMap", produces = "application/json; charset=UTF-8")
    public List<Combo> comboPhase(@RequestParam(value = "gameId") String gameId, @RequestParam(value = "levelId") String levelId) {
        List<Combo> list = new ArrayList<>();

        List<Map> auxiliarMap = mapService.findMapsByGameAndLevel(Integer.parseInt(gameId), Integer.parseInt(levelId));

        if (auxiliarMap != null && !auxiliarMap.isEmpty()) {

            List<Phase> auxiliar = phaseService.findByMap(auxiliarMap.get(0).getId());

            for (Phase item : auxiliar) {
                Combo combo = new Combo(item.getId(), item.getOrder() + ". " + item.getName());
                list.add(combo);
            }
        }

        return list;
    }

    @RequestMapping(value = "/comboQuestion", produces = "application/json; charset=UTF-8")
    public List<Combo> comboQuestion(@RequestParam(value = "phaseId") String phaseId) {
        List<Combo> list = new ArrayList<>();

        List<Question> auxiliar = questionService.findByPhase(Integer.parseInt(phaseId));

        for (Question item : auxiliar) {
            Combo combo = new Combo(item.getId(), item.getOrder() + ". " + item.getName());
            list.add(combo);
        }

        return list;
    }
}
