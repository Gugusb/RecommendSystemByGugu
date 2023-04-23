package com.gugusb.rsproject.controller;

import com.gugusb.rsproject.common.ServerResponse;
import com.gugusb.rsproject.entity.RSLog;
import com.gugusb.rsproject.entity.RSLogTeam;
import com.gugusb.rsproject.repository.RSLogRepository;
import com.gugusb.rsproject.service.RSLogService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(value="/log")
public class LogController {
    @Autowired
    RSLogService logService;
    @RequestMapping(value = "/get_all_logteams", method = RequestMethod.POST)
    public ServerResponse getAllLogTeams(HttpSession httpSession,
                                     Pageable pageable){
        return ServerResponse.createRespBySuccess(logService.getAllLogTeams(pageable));
    }

    @RequestMapping(value = "/get_logteam_inf", method = RequestMethod.POST)
    public ServerResponse getLogTeamInf(HttpSession httpSession,
                                         @RequestParam Integer teamId){
        Optional<RSLogTeam> team = logService.getLogById(teamId);
        if(team.isEmpty()){
            return ServerResponse.createByErrorMessage("No");
        }else{
            return ServerResponse.createRespBySuccess(team.get());
        }
    }

    @RequestMapping(value = "/get_logs", method = RequestMethod.POST)
    public ServerResponse getLogInfFromTeam(HttpSession httpSession,
                                        @RequestParam Integer teamId){
        List<Object> res = new Stack<>();
        Optional<RSLogTeam> team = logService.getLogById(teamId);
        //Log列表
        List<RSLog> logs = logService.getLogsByTeamNo(team.get().getTeamno());
        //用户列表
        Set<Integer> users = new HashSet<>();
        //算法列表
        Set<Integer> algs = new HashSet<>();
        for(RSLog log : logs){
            users.add(log.getUserid());
            algs.add(log.getAlgtype());
        }
        res.add(users);
        res.add(algs);
        res.add(logs);
        return ServerResponse.createRespBySuccess(res);
    }
}
