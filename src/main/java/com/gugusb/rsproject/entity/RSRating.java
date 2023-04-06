package com.gugusb.rsproject.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ratings")
public class RSRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "userid")
    private Integer userid;

    @Column(name = "movieid")
    private Integer movieid;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "timestamp")
    private Long timestamp;

    public RSRating(Integer userid, Integer movieid, Integer rating, Long timestamp) {
        this.userid = userid;
        this.movieid = movieid;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    public RSRating() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getMovieid() {
        return movieid;
    }

    public void setMovieid(Integer movieid) {
        this.movieid = movieid;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

}