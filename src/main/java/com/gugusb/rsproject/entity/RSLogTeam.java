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

    @Column(name = "userid")
    private Integer userid;

    @Column(name = "algcount")
    private Integer count;

    public RSLogTeam(Long time, Integer userid, Integer count) {
        this.time = time;
        this.userid = userid;
        this.count = count;
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

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getTeamno() {
        return teamno;
    }

    public void setTeamno(Integer teamno) {
        this.teamno = teamno;
    }
}
