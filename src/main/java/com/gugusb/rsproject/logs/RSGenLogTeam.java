package com.gugusb.rsproject.logs;

import com.gugusb.rsproject.algorithm.*;
import com.gugusb.rsproject.entity.RSLog;
import com.gugusb.rsproject.entity.RSLogTeam;
import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.entity.RSUser;

import java.util.*;

public class RSGenLogTeam {
    List<RSGenLog> logs;
    int no;
    long time;
    Set<Integer> users;
    public RSGenLogTeam(){
        this.logs = new ArrayList<>();
        //Create Nomber
        Calendar calendar = Calendar.getInstance();
        time = calendar.getTimeInMillis();
        no = Math.abs((int)time);
        users = new HashSet<>();
    }
    public RSGenLogTeam(List<RSGenLog> logs){
        this.logs = logs;
        //Create Nomber
        Calendar calendar = Calendar.getInstance();
        time = calendar.getTimeInMillis();
        no = (int)time;
        users = new HashSet<>();
        //遍历Logs以获取用户数量
        for(RSGenLog log : logs){
            users.add(log.getUser().getId());
        }
    }
    public List<RSGenLog> getLogs(){
        return this.logs;
    }
    public void addLog(RSGenLog log){
        this.logs.add(log);
        users.add(log.getUser().getId());
    }

    public RSLogTeam createLogTeamForSQL(){
        RSLogTeam team = new RSLogTeam();
        team.setAlgCount(logs.size());
        team.setTeamno(no);
        team.setTime(time);
        team.setUsercount(users.size());
        return team;
    }
    public List<RSLog> createLogForSQL(){
        List<RSLog> rsLogs = new ArrayList<>();
        for(RSGenLog log : logs){
            RSLog rsLog = new RSLog(log.getUser().getId(), log.getRecall(), log.getPrecision(), log.getAccuracy(), null, null, null);
            if(log.getAlg() instanceof UCF_Alg){
                rsLog.setAlgtype(2);
            }else if(log.getAlg() instanceof CB_Alg) {
                rsLog.setAlgtype(1);
            }else if(log.getAlg() instanceof ICF_Alg) {
                rsLog.setAlgtype(3);
            }else if(log.getAlg() instanceof MixAlg_2) {
                rsLog.setAlgtype(4);
            }
            rsLog.setTime(log.time.getTime());
            rsLog.setTeamid(no);
            rsLogs.add(rsLog);
        }
        return rsLogs;
    }
}
