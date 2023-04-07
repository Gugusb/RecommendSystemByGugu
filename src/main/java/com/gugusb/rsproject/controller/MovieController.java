package com.gugusb.rsproject.controller;

import com.gugusb.rsproject.common.ServerResponse;
import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.service.RSGenerService;
import com.gugusb.rsproject.service.RSMovieService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/movie")
public class MovieController {
    @Autowired
    RSMovieService movieService;
    @Autowired
    RSGenerService generService;
    //基础操作
    @RequestMapping(value = "/add_movie", method = RequestMethod.POST)
    public ServerResponse addMovie(HttpSession httpSession, RSMovie movie, Integer[] genres){
        movieService.addMovie(movie, genres);
        generService.addMovie(genres);
        return ServerResponse.createRespBySuccess("Add succeed");
    }
    @RequestMapping(value = "/delete_movie", method = RequestMethod.POST)
    public ServerResponse deleteMovie(HttpSession httpSession, Integer movieid){
        if(movieService.deleteMovie(movieid)){
            if(generService.deleteMovie(movieid)){
                return ServerResponse.createRespBySuccess("Delete succeed");
            }
        }
        return ServerResponse.createRespByError();
    }
    @RequestMapping(value = "/update_movie", method = RequestMethod.POST)
    public String updateMovie(HttpSession httpSession, Integer id){
        return "123";
    }
}
