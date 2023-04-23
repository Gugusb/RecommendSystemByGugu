package com.gugusb.rsproject.controller;

import com.gugusb.rsproject.algorithm.*;
import com.gugusb.rsproject.common.ServerResponse;
import com.gugusb.rsproject.div_strategy.HO_Stra;
import com.gugusb.rsproject.entity.RSLog;
import com.gugusb.rsproject.entity.RSMovie;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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
    @Autowired
    RSLogService logService;

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

    HO_Stra one_stra = new HO_Stra();
    @RequestMapping(value = "/test/alg", method = RequestMethod.POST)
    public String AlgTest(HttpSession httpSession, Integer userid){
        RSUser user = new RSUser();
        user.setId(userid);

        //划分训练集和测试集
        HO_Stra ho_stra = one_stra;

        //获取算法对象
        //注入训练集
        CB_Alg cfAlg = algorithmService.getCFAlg(user,
                ratingService.getRatedMovieByUserFromTrainSet(user, ho_stra),
                ratingService.getRatingMapForUserFromTarinSet(user, ho_stra),
                GenreTransformer.CreateGenreMap(generService.getAllMovieList()),
                ho_stra);

        //计算结果
        for(MovieWithRate movieWithRate :
                cfAlg.getRecommandMovie()){
            System.out.println("" + movieWithRate.getMovieId()  + " " + movieWithRate.getRate());
        }

        //测试结果准确性
        //注入测试集
        double p = 0.0, r = 0.0, a = 0.0;
        List<RSMovie> testMovieSet = ratingService.getLikeMovieByUserWithTestSet(user, ho_stra);
        p = cfAlg.getPrecision(testMovieSet);
        r = cfAlg.getRecall(testMovieSet);
        a = cfAlg.getAccuracy(testMovieSet);
        return "Precision:" + p + " Recall:" + r + " Accuracy:" + a;
    }

    //==========================UCF测试==========================
    UCF_Alg ucf_alg;
    RSUser ucf_user;
    HO_Stra ucf_stra;

    @RequestMapping(value = "/test/ucfinit", method = RequestMethod.POST)
    public String InitUCF(HttpSession httpSession, Integer userid){
        ucf_user = new RSUser();
        ucf_user.setId(userid);
        ucf_stra = one_stra;
        ucf_alg = algorithmService.getUCFAlg(ratingService.getAllRatingPage(ucf_stra), ucf_user);
        return "UCF finish init";
    }

    @RequestMapping(value = "/test/ucftest", method = RequestMethod.POST)
    public String getSim(HttpSession httpSession){
        for(MovieWithRate movie : ucf_alg.getRecommandMovie()){
            System.out.println("" + movie.getMovieId() + " " + movie.getRate());
        }
        return "finish";
    }

    @RequestMapping(value = "/test/ucfresult", method = RequestMethod.POST)
    public String ResultUcf(HttpSession httpSession){
        double p = 0.0, r = 0.0, a = 0.0;
        List<RSMovie> testMovieSet = ratingService.getLikeMovieByUserWithTestSet(ucf_user, ucf_stra);
        p = ucf_alg.getPrecision(testMovieSet);
        r = ucf_alg.getRecall(testMovieSet);
        a = ucf_alg.getAccuracy(testMovieSet);
        return "Precision:" + p + " Recall:" + r + " Accuracy:" + a;
    }

    //==========================ICF测试==========================
    ICF_Alg icf_alg;

    @RequestMapping(value = "/test/initicf", method = RequestMethod.POST)
    public String InitICF(HttpSession httpSession, Integer userid){
        RSUser user = new RSUser();
        user.setId(userid);
        HO_Stra ho_stra = new HO_Stra();
        icf_alg = algorithmService.getICFAlg(ratingService.getAllRatingPage(ho_stra), ratingService.getCoMatrixProMax(), ratingService.getSpawnCount(ho_stra), user);
        for(MovieWithRate movie : icf_alg.getRecommandMovie()){
            System.out.println("" + movie.getMovieId() + " " + movie.getRate());
        }
        return "ICF finish init";
    }

    @RequestMapping(value = "/test/com", method = RequestMethod.POST)
    public ServerResponse testCoM(HttpSession httpSession,
                                  Integer id1,
                                  Integer id2){
        return ServerResponse.createRespBySuccess(ratingService.getCoMatrixProMax()[id1][id2]);
    }
    //==========================Mix1测试==========================
    //测试一下生成限定电影集合的共现矩阵的运行速度
    @RequestMapping(value = "/test/cmt", method = RequestMethod.POST)
    public String CMTest1(HttpSession httpSession, Integer movie1, Integer movie2){
        List<MovieWithRate> movieList = new ArrayList<>();
        for(int i = movie1;i < movie2;i ++){
            movieList.add(new MovieWithRate(i, 5));
        }
        int[][] cm = ratingService.getCoMatrixByMovieList(movieList);
        return "finish" + cm[movie1][movie1 + 1];
    }

    MixAlg_1 mixAlg1;
    HO_Stra mix1_stra;
    RSUser mix1_user;
    @RequestMapping(value = "/test/mix1init", method = RequestMethod.POST)
    public String InitMix1(HttpSession httpSession, Integer userid){
        mix1_stra = one_stra;
        mix1_user = new RSUser();
        mix1_user.setId(userid);
        mixAlg1 = algorithmService.getMixAlg_1(ratingService.getAllRatingPage(mix1_stra), ratingService.getSpawnCount(mix1_stra), mix1_stra, mix1_user);
        return "Init mixalg1 finish";
    }

    @RequestMapping(value = "/test/mix1test", method = RequestMethod.POST)
    public String Mix1Test(HttpSession httpSession){
        for(MovieWithRate movie : mixAlg1.getRecommandMovie()){
            System.out.println("" + movie.getMovieId() + " " + movie.getRate());
        }
        return "finish";
    }

    @RequestMapping(value = "/test/mix1result", method = RequestMethod.POST)
    public String ResultMix1(HttpSession httpSession){
        double p = 0.0, r = 0.0, a = 0.0;
        List<RSMovie> testMovieSet = ratingService.getLikeMovieByUserWithTestSet(mix1_user, mix1_stra);
        p = mixAlg1.getPrecision(testMovieSet);
        r = mixAlg1.getRecall(testMovieSet);
        a = mixAlg1.getAccuracy(testMovieSet);
        return "Precision:" + p + " Recall:" + r + " Accuracy:" + a;
    }

    //==========================Mix2测试==========================
    MixAlg_2 mixAlg2;
    HO_Stra mix2_stra;
    RSUser mix2_user;
    @RequestMapping(value = "/test/mix2init", method = RequestMethod.POST)
    public String InitMix2(HttpSession httpSession, Integer userid){
        mix2_stra = one_stra;
        mix2_user = new RSUser();
        mix2_user.setId(userid);
        mixAlg2 = algorithmService.getMixAlg_2(ratingService.getAllRatingPage(mix2_stra), mix2_stra, GenreTransformer.CreateGenreMap(generService.getAllMovieList()), mix2_user);
        return "Init mixalg2 finish";
    }

    @RequestMapping(value = "/test/mix2test", method = RequestMethod.POST)
    public String Mix2Test(HttpSession httpSession){
        for(MovieWithRate movie : mixAlg2.getRecommandMovie()){
            System.out.println("" + movie.getMovieId() + " " + movie.getRate());
        }
        return "finish";
    }

    @RequestMapping(value = "/test/mix2result", method = RequestMethod.POST)
    public String ResultMix2(HttpSession httpSession){
        double p = 0.0, r = 0.0, a = 0.0;
        List<RSMovie> testMovieSet = ratingService.getLikeMovieByUserWithTestSet(mix2_user, mix2_stra);
        p = mixAlg2.getPrecision(testMovieSet);
        r = mixAlg2.getRecall(testMovieSet);
        a = mixAlg2.getAccuracy(testMovieSet);
        return "Precision:" + p + " Recall:" + r + " Accuracy:" + a;
    }

    //==========================Log测试==========================
    @RequestMapping(value = "/test/logtest", method = RequestMethod.POST)
    public String Log1(HttpSession httpSession){
        RSLog rsLog = new RSLog(1, 0.6, 0.7, 0.8, 1, System.currentTimeMillis(), 1);
        logService.addLog(rsLog);
        return "ok";
    }
}
