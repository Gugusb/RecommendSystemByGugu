package com.gugusb.rsproject.controller;

import com.gugusb.rsproject.common.ServerResponse;
import com.gugusb.rsproject.entity.MovieWithGenres;
import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.entity.RSRating;
import com.gugusb.rsproject.service.RSGenerService;
import com.gugusb.rsproject.service.RSMovieService;
import com.gugusb.rsproject.service.RSRatingService;
import jakarta.persistence.Index;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Stack;

@RestController
@RequestMapping(value="/movie")
public class MovieController {
    @Autowired
    RSMovieService movieService;
    @Autowired
    RSGenerService generService;
    @Autowired
    RSRatingService ratingService;
    //基础操作
    @RequestMapping(value = "/add_movie", method = RequestMethod.POST)
    public ServerResponse addMovie(HttpSession httpSession, RSMovie movie, Integer[] genres){
        movieService.addMovie(movie, genres);
        generService.addMovie(genres);
        return ServerResponse.createRespBySuccess("Add succeed");
    }

    @RequestMapping(value = "/update_movie", method = RequestMethod.POST)
    public String updateMovie(HttpSession httpSession, Integer id){
        return "123";
    }

    @RequestMapping(value = "/search_movie_count", method = RequestMethod.POST)
    public ServerResponse<Long> serachMovieCount(HttpSession httpSession,
                                                             @RequestParam List<Integer> tags,
                                                             @RequestParam Boolean isAsc,
                                                             Pageable pageable){
        return ServerResponse.createRespBySuccess(generService.searchMovieByTagsOrderById(pageable, tags, isAsc).getTotalElements());
    }

    @RequestMapping(value = "/search_movie_inf", method = RequestMethod.POST)
    public ServerResponse<List<RSMovie>> serachMovie(HttpSession httpSession,
                                                             @RequestParam List<Integer> tags,
                                                             @RequestParam Boolean isAsc,
                                                             Pageable pageable){
        //拿到所有电影ID
        Page<MovieWithGenres> movieWithGenres = generService.searchMovieByTagsOrderById(pageable, tags, isAsc);
        //拿到所有电影实体
        List<RSMovie> movies = movieService.getMovieFromGenres(movieWithGenres);
        return ServerResponse.createRespBySuccess(movies);
    }

    @RequestMapping(value = "/search_movie_ratings", method = RequestMethod.POST)
    public ServerResponse<List<Integer>> serachMovieRatings(HttpSession httpSession,
                                                 @RequestParam Integer movieId){
        return ServerResponse.createRespBySuccess(ratingService.getScoreRatingByMovieId(movieId));
    }

    @RequestMapping(value = "/search_movies", method = RequestMethod.POST)
    public ServerResponse serachMovies(HttpSession httpSession,
                                                      @RequestParam Integer movieId,
                                                      Pageable pageable){
        if(movieId <= 0){
            return ServerResponse.createRespBySuccess(movieService.getAllMovies(pageable));
        }else{
            return ServerResponse.createRespBySuccess(movieService.getMovieById(movieId, pageable));
        }
    }

    @RequestMapping(value = "/delete_movie", method = RequestMethod.POST)
    public ServerResponse deleteMovie(HttpSession httpSession,
                                       @RequestParam Integer movieId){
        if(movieService.deleteMovie(movieId)){
            if(generService.deleteMovie(movieId)){
                return ServerResponse.createRespBySuccess("Okk");
            }
        }
        return ServerResponse.createByErrorMessage("No");
    }

    @RequestMapping(value = "/get_movie_genres", method = RequestMethod.POST)
    public ServerResponse getMovieGenres(HttpSession httpSession,
                                      @RequestParam Integer movieId){
        MovieWithGenres mwr = generService.getMovieGenresById(movieId);
        if(mwr != null){
            List<Integer> res = generService.getMovieMap(mwr);
            return ServerResponse.createRespBySuccess(res);
        }
        return ServerResponse.createByErrorMessage("1");
    }

    @RequestMapping(value = "/change_movie_inf", method = RequestMethod.POST)
    public ServerResponse changeMovieInf(HttpSession httpSession,
                                         @RequestParam Integer movieId,
                                         @RequestParam String newName,
                                         @RequestParam List<Integer> newGenres){
        if(newName == "null"){
            newName = "";
        }
        if(newGenres.get(0) == -1){
            newGenres = new Stack<>();
        }
        if(movieService.updateMovieInf(movieId, newName, newGenres)){
            if(generService.updateMovieInf(movieId, newGenres)){
                ServerResponse.createRespBySuccess("OKK");
            }
        }
        return ServerResponse.createByErrorMessage("NOOO");
    }
}
