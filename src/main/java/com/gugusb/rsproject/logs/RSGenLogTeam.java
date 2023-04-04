package com.gugusb.rsproject.logs;

import com.gugusb.rsproject.algorithm.*;
import com.gugusb.rsproject.entity.RSLog;
import com.gugusb.rsproject.entity.RSLogTeam;
import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.entity.RSUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class RSGenLogTeam {
    List<RSGenLog> logs;
    int no;
    long time;
    public RSGenLogTeam(){
        this.logs = new ArrayList<>();
        //Create Nomber
        Calendar calendar = Calendar.getInstance();
        time = calendar.getTimeInMillis();
        no = (int)time;
    }
    public RSGenLogTeam(List<RSGenLog> logs){
        this.logs = logs;
        //Create Nomber
        Calendar calendar = Calendar.getInstance();
        time = calendar.getTimeInMillis();
        no = (int)time;
    }
    public List<RSGenLog> getLogs(){
        return this.logs;
    }
    public void addLog(RSGenLog log){
        this.logs.add(log);
    }

    public RSLogTeam createLogTeamForSQL(){
        RSLogTeam team = new RSLogTeam();
        team.setCount(logs.size());
        team.setTeamno(no);
        team.setTime(time);
        team.setUserid(logs.get(0).getUser().getId());
        return team;
    }
    public List<RSLog> createLogForSQL(){
        List<RSLog> rsLogs = new ArrayList<>();
        for(RSGenLog log : logs){
            RSLog rsLog = new RSLog(log.getUser().getId(), log.getRecall(), log.getPrecision(), log.getAccuracy(), null, null, null);
            if(log.getAlg() instanceof UCF_Alg){
                rsLog.setAlgtype("UCF");
            }else if(log.getAlg() instanceof CB_Alg) {
                rsLog.setAlgtype("CB");
            }else if(log.getAlg() instanceof MixAlg_1) {
                rsLog.setAlgtype("M1");
            }else if(log.getAlg() instanceof MixAlg_2) {
                rsLog.setAlgtype("M2");
            }else if(log.getAlg() instanceof MixAlg_3) {
                rsLog.setAlgtype("M3");
            }
            rsLog.setTime(log.time.getTime());
            rsLog.setTeamid(no);
            rsLogs.add(rsLog);
        }
        return rsLogs;
    }
}
