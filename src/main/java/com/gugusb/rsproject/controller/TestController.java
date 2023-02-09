package com.gugusb.rsproject.controller;

import com.gugusb.rsproject.algorithm.CF_Alg;
import com.gugusb.rsproject.division_strategy.BaseStra;
import com.gugusb.rsproject.division_strategy.HO_Strategy;
import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.entity.RSUser;
import com.gugusb.rsproject.service.*;
import com.gugusb.rsproject.util.GenreTransformer;
import com.gugusb.rsproject.util.MovieWithRate;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    @Autowired
    RSUserService userService;

    @Autowired
    RSUserInfService userInfService;
    @Autowired
    DivStrategyService divStrategyService;
    @Autowired
    AlgorithmService algorithmService;
    @Autowired
    RSRatingService ratingService;
    @Autowired
    RSMovieService movieService;
    @Autowired
    RSGenerService generService;

    @RequestMapping(value = "/test/test", method = RequestMethod.POST)
    public String test(HttpSession httpSession, Integer id){
        return userInfService.getGenderById(id);
    }

    @RequestMapping(value = "/test/division", method = RequestMethod.POST)
    public String division(HttpSession httpSession){
        List<RSMovie> testmovies = divStrategyService.getDivStrategy(1).getTestSet();
        List<RSMovie> trainmovies = divStrategyService.getDivStrategy(1).getTrainingSet();
        divStrategyService.getDivStrategy(1).reGenerateSets();
        return "" + testmovies.size() + " " + trainmovies.size() + " " + (testmovies.size() + trainmovies.size());
    }
    @RequestMapping(value = "/test/alg", method = RequestMethod.POST)
    public String AlgTest(HttpSession httpSession, Integer userid, Integer tarid){
        RSUser user = new RSUser();
        user.setId(userid);
        //获取算法对象
        CF_Alg cfAlg = algorithmService.getCFAlg(user,
                ratingService.getRatedMovieByUser(user),
                ratingService.getRatingMapForUser(user));

        //划分训练集和测试集
        List<RSMovie> testmovies = divStrategyService.getDivStrategy(1).getTestSet();
        List<RSMovie> trainmovies = divStrategyService.getDivStrategy(1).getTrainingSet();
        divStrategyService.getDivStrategy(1).reGenerateSets();

        for(MovieWithRate movieWithRate :
                cfAlg.getRecommandMovie(GenreTransformer.CreateGenreMap(generService.TransMovieToMovieWithGenre(trainmovies)))){
            System.out.println("" + movieWithRate.getMovieId()  + " " + movieWithRate.getRate());
        }

        return "suc";
    }

}
