package com.gugusb.rsproject.service;

import com.gugusb.rsproject.entity.RSLog;
import com.gugusb.rsproject.entity.RSLogTeam;
import com.gugusb.rsproject.repository.RSLogRepository;
import com.gugusb.rsproject.repository.RSLogTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RSLogService {
    @Autowired
    RSLogRepository logRepository;
    @Autowired
    RSLogTeamRepository logTeamRepository;

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
