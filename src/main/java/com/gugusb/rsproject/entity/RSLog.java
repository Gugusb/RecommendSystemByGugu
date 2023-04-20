package com.gugusb.rsproject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.sql.Timestamp;

@Entity
@Table(name = "logs")
public class RSLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "loguserid")
    private Integer userid;

    @Column(name = "logrecall")
    private Double recall;

    @Column(name = "logprecision")
    private Double precision;

    @Column(name = "logaccuracy")
    private Double accuracy;

    @Size(max = 20)
    @Column(name = "logalgtype")
    private Integer algtype;

    @Column(name = "logtimestamp")
    private Long time;

    @Column(name = "logteamid")
    private Integer teamid;

    public RSLog() {

    }

    public RSLog(Integer userid, Double recall, Double precision, Double accuracy, Integer algtype, Long time, Integer teamid) {
        this.userid = userid;
        this.recall = recall;
        this.precision = precision;
        this.accuracy = accuracy;
        this.algtype = algtype;
        this.time = time;
        this.teamid = teamid;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserid() {
        return userid;
    }

    public Double getRecall() {
        return recall;
    }

    public Double getPrecision() {
        return precision;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public Integer getAlgtype() {
        return algtype;
    }

    public Long getTime() {
        return time;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public void setRecall(Double recall) {
        this.recall = recall;
    }

    public void setPrecision(Double precision) {
        this.precision = precision;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public void setAlgtype(Integer algtype) {
        this.algtype = algtype;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Integer getTeamid() {
        return teamid;
    }

    public void setTeamid(Integer teamid) {
        this.teamid = teamid;
    }
}