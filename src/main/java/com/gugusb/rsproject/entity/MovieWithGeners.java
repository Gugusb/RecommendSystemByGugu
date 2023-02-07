package com.gugusb.rsproject.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "movie_genersplus")
public class MovieWithGeners {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "genercount")
    private Byte genercount;

    @Column(name = "Action")
    private Byte action;

    @Column(name = "Adventure")
    private Byte adventure;

    @Column(name = "Animation")
    private Byte animation;

    @Column(name = "Childrens")
    private Byte childrens;

    @Column(name = "Comedy")
    private Byte comedy;

    @Column(name = "Crime")
    private Byte crime;

    @Column(name = "Documentary")
    private Byte documentary;

    @Column(name = "Drama")
    private Byte drama;

    @Column(name = "Fantasy")
    private Byte fantasy;

    @Column(name = "FilmNoir")
    private Byte filmNoir;

    @Column(name = "Horror")
    private Byte horror;

    @Column(name = "Musical")
    private Byte musical;

    @Column(name = "Mystery")
    private Byte mystery;

    @Column(name = "Romance")
    private Byte romance;

    @Column(name = "SciFi")
    private Byte sciFi;

    @Column(name = "Thriller")
    private Byte thriller;

    @Column(name = "War")
    private Byte war;

    @Column(name = "Western")
    private Byte western;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getGenercount() {
        return genercount;
    }

    public void setGenercount(Byte genercount) {
        this.genercount = genercount;
    }

    public Byte getAction() {
        return action;
    }

    public void setAction(Byte action) {
        this.action = action;
    }

    public Byte getAdventure() {
        return adventure;
    }

    public void setAdventure(Byte adventure) {
        this.adventure = adventure;
    }

    public Byte getAnimation() {
        return animation;
    }

    public void setAnimation(Byte animation) {
        this.animation = animation;
    }

    public Byte getChildrens() {
        return childrens;
    }

    public void setChildrens(Byte childrens) {
        this.childrens = childrens;
    }

    public Byte getComedy() {
        return comedy;
    }

    public void setComedy(Byte comedy) {
        this.comedy = comedy;
    }

    public Byte getCrime() {
        return crime;
    }

    public void setCrime(Byte crime) {
        this.crime = crime;
    }

    public Byte getDocumentary() {
        return documentary;
    }

    public void setDocumentary(Byte documentary) {
        this.documentary = documentary;
    }

    public Byte getDrama() {
        return drama;
    }

    public void setDrama(Byte drama) {
        this.drama = drama;
    }

    public Byte getFantasy() {
        return fantasy;
    }

    public void setFantasy(Byte fantasy) {
        this.fantasy = fantasy;
    }

    public Byte getFilmNoir() {
        return filmNoir;
    }

    public void setFilmNoir(Byte filmNoir) {
        this.filmNoir = filmNoir;
    }

    public Byte getHorror() {
        return horror;
    }

    public void setHorror(Byte horror) {
        this.horror = horror;
    }

    public Byte getMusical() {
        return musical;
    }

    public void setMusical(Byte musical) {
        this.musical = musical;
    }

    public Byte getMystery() {
        return mystery;
    }

    public void setMystery(Byte mystery) {
        this.mystery = mystery;
    }

    public Byte getRomance() {
        return romance;
    }

    public void setRomance(Byte romance) {
        this.romance = romance;
    }

    public Byte getSciFi() {
        return sciFi;
    }

    public void setSciFi(Byte sciFi) {
        this.sciFi = sciFi;
    }

    public Byte getThriller() {
        return thriller;
    }

    public void setThriller(Byte thriller) {
        this.thriller = thriller;
    }

    public Byte getWar() {
        return war;
    }

    public void setWar(Byte war) {
        this.war = war;
    }

    public Byte getWestern() {
        return western;
    }

    public void setWestern(Byte western) {
        this.western = western;
    }

}