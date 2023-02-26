package com.gugusb.rsproject.controller;

import com.gugusb.rsproject.algorithm.CB_Alg;
import com.gugusb.rsproject.algorithm.UCF_Alg;
import com.gugusb.rsproject.div_strategy.HO_Stra;
import com.gugusb.rsproject.entity.RSRating;
import com.gugusb.rsproject.entity.RSUser;
import com.gugusb.rsproject.service.*;
import com.gugusb.rsproject.util.GenreTransformer;
import com.gugusb.rsproject.util.MovieWithRate;
import com.gugusb.rsproject.util.UserWithRate;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

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
        Set<RSRating> testratings = divStrategyService.getDivStrategy(1).getTestSet();
        Set<RSRating> trainratings = divStrategyService.getDivStrategy(1).getTrainingSet();
        divStrategyService.getDivStrategy(1).reGenerateSets();
        return "" + testratings.size() + " " + trainratings.size() + " " + (testratings.size() + trainratings.size());
    }
    @RequestMapping(value = "/test/alg", method = RequestMethod.POST)
    public String AlgTest(HttpSession httpSession, Integer userid){
        RSUser user = new RSUser();
        user.setId(userid);

        //划分训练集和测试集
        HO_Stra ho_stra = new HO_Stra();

        /*
        划分训练集和测试集的新思想
        创建一个数据划分对象，该对象每次实例化时会生成一个较大的随机数种子Seed
        当需要判断一个RatingId属于测试集或训练集时，将RatingId和随机数种子Seed相加
        之后将加和的数字作为最终种子计算出0-1的随机数，取该随机数的最后几位加以位权得到新的0-1的最终数据
        通过该数和比例的大小关系判断是否属于测试集或者训练集
        */

        //获取算法对象
        //注入训练集
        CB_Alg cfAlg = algorithmService.getCFAlg(user,
                ratingService.getRatedMovieByUserFromTrainSet(user, ho_stra),
                ratingService.getRatingMapForUserFromTarinSet(user, ho_stra));

        //计算结果
        for(MovieWithRate movieWithRate :
                cfAlg.getRecommandMovie(GenreTransformer.CreateGenreMap(generService.getAllMovieList()))){
            System.out.println("" + movieWithRate.getMovieId()  + " " + movieWithRate.getRate());
        }

        //测试结果准确性
        //注入测试集

        return "suc";
    }

    UCF_Alg ucf_alg;

    @RequestMapping(value = "/test/initucf", method = RequestMethod.POST)
    public String InitUCF(HttpSession httpSession, Integer userid){
        RSUser user = new RSUser();
        user.setId(userid);
        HO_Stra ho_stra = new HO_Stra();
        ucf_alg = algorithmService.getUCFAlg(ratingService.getAllRatingPage(ho_stra), user);
        return "finish init";
    }

    @RequestMapping(value = "/test/sim", method = RequestMethod.POST)
    public String getSim(HttpSession httpSession){
        for(MovieWithRate movie : ucf_alg.getRecommandMovie(null)){
            System.out.println("" + movie.getMovieId() + " " + movie.getRate());
        }
        return "finish";
    }

}
