package com.gugusb.rsproject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class RSUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 5)
    @Column(name = "gender", length = 5)
    private String gender;

    @Column(name = "age")
    private Integer age;

    @Column(name = "occopation")
    private Integer occupation;

    @Size(max = 15)
    @Column(name = "zipdode", length = 15)
    private String zipdode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getOccupation() {
        return occupation;
    }

    public void setOccupation(Integer occupation) {
        this.occupation = occupation;
    }

    public String getZipdode() {
        return zipdode;
    }

    public void setZipdode(String zipdode) {
        this.zipdode = zipdode;
    }

}