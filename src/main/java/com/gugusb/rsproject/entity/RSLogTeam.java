package com.gugusb.rsproject.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "logteams")
public class RSLogTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "teamno")
    private Integer teamno;

    @Column(name = "createtime")
    private Long time;

    @Column(name = "usercount")
    private Integer usercount;

    @Column(name = "algcount")
    private Integer algcount;

    public RSLogTeam(Long time, Integer usercount, Integer algcount) {
        this.time = time;
        this.usercount = usercount;
        this.algcount = algcount;
    }

    public RSLogTeam() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Integer getUsercount() {
        return usercount;
    }

    public void setUsercount(Integer usercount) {
        this.usercount= usercount;
    }

    public Integer getAlgCount() {
        return algcount;
    }

    public void setAlgCount(Integer count) {
        this.algcount = count;
    }

    public Integer getTeamno() {
        return teamno;
    }

    public void setTeamno(Integer teamno) {
        this.teamno = teamno;
    }
}
