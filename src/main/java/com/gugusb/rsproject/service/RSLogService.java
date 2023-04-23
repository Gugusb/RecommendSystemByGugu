package com.gugusb.rsproject.service;

import com.gugusb.rsproject.entity.RSLog;
import com.gugusb.rsproject.entity.RSLogTeam;
import com.gugusb.rsproject.repository.RSLogRepository;
import com.gugusb.rsproject.repository.RSLogTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RSLogService {
    @Autowired
    RSLogRepository logRepository;
    @Autowired
    RSLogTeamRepository logTeamRepository;

    public List<RSLog> getLogsByTeamNo(Integer teamNo){
        return logRepository.findByTeamid(teamNo);
    }
    public Optional<RSLogTeam> getLogById(Integer teamId){
        return logTeamRepository.findById(teamId);
    }
    public Page<RSLogTeam> getAllLogTeams(Pageable pageable){
        return logTeamRepository.findAll(pageable);
    }
    public void addLog(RSLog log){
        logRepository.save(log);
    }
    public void addLogs(List<RSLog> logs){
        for(RSLog log : logs){
            logRepository.save(log);
        }
    }
    public void addLogTeam(RSLogTeam logTeam){
        logTeamRepository.save(logTeam);
    }
}
