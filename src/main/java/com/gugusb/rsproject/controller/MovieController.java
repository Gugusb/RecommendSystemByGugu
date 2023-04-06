package com.gugusb.rsproject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/movie")
public class MovieController {
    //基础操作
    @RequestMapping(value = "/add_movie", method = RequestMethod.POST)
    public String addMovie(HttpSession httpSession, Integer id){
        return "123";
    }
    @RequestMapping(value = "/delete_movie", method = RequestMethod.POST)
    public String deleteMovie(HttpSession httpSession, Integer id){
        return "123";
    }
    @RequestMapping(value = "/update_movie", method = RequestMethod.POST)
    public String updateMovie(HttpSession httpSession, Integer id){
        return "123";
    }
}
