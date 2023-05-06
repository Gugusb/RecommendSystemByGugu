package com.gugusb.rsproject.controller;

import com.gugusb.rsproject.algorithm.*;
import com.gugusb.rsproject.common.ServerResponse;
import com.gugusb.rsproject.div_strategy.HO_Stra;
import com.gugusb.rsproject.entity.MovieWithGenres;
import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.entity.RSUser;
import com.gugusb.rsproject.logs.RSGenLog;
import com.gugusb.rsproject.logs.RSGenLogTeam;
import com.gugusb.rsproject.service.*;
import com.gugusb.rsproject.util.ConstUtil;
import com.gugusb.rsproject.util.GenreTransformer;
import com.gugusb.rsproject.util.MovieWithRate;
import io.swagger.models.auth.In;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(value="/recommend")
public class RecommendController {
    @Autowired
    AlgorithmService algorithmService;
    @Autowired
    RSRatingService ratingService;
    @Autowired
    RSGenerService generService;
    @Autowired
    RSLogService logService;
    @Autowired
    RSMovieService movieService;

    @RequestMapping(value = "/recommend", method = RequestMethod.POST)
    public ServerResponse recommend(HttpSession httpSession,
                                    @RequestParam List<Integer> users,
                                    @RequestParam List<Boolean> algBooleans,
                                    @RequestParam Boolean isCompensate,
                                    @RequestParam Double rate,
                                    @RequestParam List<Integer> genres){
        //Step0.优化输入数据
        Set<Integer> userSet = new HashSet<>();
        for(int i : users){
            if(i > 0 && i < ConstUtil.USER_COUNT){
                userSet.add(i);
            }
        }
        users = new Stack<>();
        for(int i : userSet){
            users.add(i);
        }
        List<Integer> algs = new Stack<>();
        for(int i = 1;i <= algBooleans.size();i ++){
            if(algBooleans.get(i - 1)){
                algs.add(i);
            }
        }
        if(users.size() <= 0){
            return ServerResponse.createByErrorMessage("No users are used to recommend!");
        }
        //Step1.划分测试集和训练集
        HO_Stra div_stra = new HO_Stra(rate);

        //Step2.构建用户实体
        List<RSUser> rsUsers = new Stack<>();
        for(int i : users){
            RSUser user = new RSUser();
            user.setId(i);
            rsUsers.add(user);
        }

        System.out.println("Step2 Finish");


        //Step3.为每个用户对象初始化推荐算法对象 注入训练集
        //Step4.输出推荐的电影集
        Map<Integer,Set<Integer>> compensateMoviesMap = new HashMap<>();//用于补偿的电影集合
        List<MovieWithRate> resMovies = new Stack<>();//用于输出的电影集合
        Map<Integer, Map<Integer, BaseAlg>> userMap = new HashMap<>();
        Map<Integer, Map<Integer, List<MovieWithRate>>> userResMap = new HashMap<>();
        for(RSUser user : rsUsers){
            Set<Integer> compensateMovies = new HashSet<>();//用于补偿的电影集合
            Map<Integer, BaseAlg> algMap = new HashMap<>();
            Map<Integer, List<MovieWithRate>> algResMap = new HashMap<>();
            for(int i : algs){
                if(i == 1){
                    //-0-CB算法
                    CB_Alg cfAlg;
                    if(genres == null || genres.size() <= 0){
                        cfAlg = algorithmService.getCFAlg(user,
                                ratingService.getRatedMovieByUserFromTrainSet(user, div_stra),
                                ratingService.getRatingMapForUserFromTarinSet(user, div_stra),
                                GenreTransformer.CreateGenreMap(generService.getAllMovieList()),
                                div_stra);
                    }else{
                        cfAlg = algorithmService.getNUAlg(user,
                                ratingService.getRatedMovieByUserFromTrainSet(user, div_stra),
                                ratingService.getRatingMapForUserFromTarinSet(user, div_stra),
                                GenreTransformer.CreateGenreMap(generService.getAllMovieList()),
                                div_stra,
                                genres);
                    }

                    List<MovieWithRate> rm = cfAlg.getRecommandMovie();
                    algMap.put(i, cfAlg);
                    algResMap.put(i, rm);
                    //如果是单用户推荐 则返回电影列表
                    if(users.size() == 1){
                        resMovies.addAll(rm.subList(0, Math.min(5, rm.size())));
                    }
                    //如果开启了算法补偿，则记录推荐的电影
                    if(isCompensate){
                        for(MovieWithRate movieWithRate : rm){
                            compensateMovies.add(movieWithRate.getMovieId());
                        }
                    }
                }else if(i == 2){
                    //-1-UCF算法
                    UCF_Alg ucf_alg = algorithmService.getUCFAlg(ratingService.getAllRatingPage(div_stra), user);
                    algMap.put(i, ucf_alg);
                    List<MovieWithRate> rm = ucf_alg.getRecommandMovie();
                    algResMap.put(i, rm);
                    //如果是单用户推荐 则返回电影列表
                    if(users.size() == 1){
                        resMovies.addAll(rm.subList(0, Math.min(5, rm.size())));
                    }
                    //如果开启了算法补偿，则记录推荐的电影
                    if(isCompensate){
                        for(MovieWithRate movieWithRate : rm){
                            compensateMovies.add(movieWithRate.getMovieId());
                        }
                    }
                }else if(i == 3){
                    //-2-ICF算法
                    ICF_Alg icf_alg = algorithmService.getICFAlg(ratingService.getAllRatingPage(div_stra), ratingService.getCoMatrixProMax(), ratingService.getSpawnCount(div_stra), user);
                    algMap.put(i, icf_alg);
                    List<MovieWithRate> rm = icf_alg.getRecommandMovie();
                    algResMap.put(i, rm);
                    //如果是单用户推荐 则返回电影列表
                    if(users.size() == 1){
                        resMovies.addAll(rm.subList(0, Math.min(5, rm.size())));
                    }
                    //如果开启了算法补偿，则记录推荐的电影
                    if(isCompensate){
                        for(MovieWithRate movieWithRate : rm){
                            compensateMovies.add(movieWithRate.getMovieId());
                        }
                    }
                }else if(i == 4){
                    //-3-MIX算法
                    MixAlg_2 mixAlg2 = algorithmService.getMixAlg_2(ratingService.getAllRatingPage(div_stra), div_stra, GenreTransformer.CreateGenreMap(generService.getAllMovieList()), user);
                    algMap.put(i, mixAlg2);
                    List<MovieWithRate> rm = mixAlg2.getRecommandMovie();
                    algResMap.put(i, rm);
                    //如果是单用户推荐 则返回电影列表
                    if(users.size() == 1){
                        resMovies.addAll(rm.subList(0, Math.min(5, rm.size())));
                    }
                    //如果开启了算法补偿，则记录推荐的电影
                    if(isCompensate){
                        for(MovieWithRate movieWithRate : rm){
                            compensateMovies.add(movieWithRate.getMovieId());
                        }
                    }
                }
            }
            userMap.put(user.getId(), algMap);
            userResMap.put(user.getId(), algResMap);
            compensateMoviesMap.put(user.getId(), compensateMovies);
        }
        //Step5.计算推荐结果评判标准 作为Log存储进数据库
        //创建Log组
        RSGenLogTeam logTeam = new RSGenLogTeam();
        for(RSUser user : rsUsers){
            //获取测试集
            List<RSMovie> testMovieSet = ratingService.getLikeMovieByUserWithTestSet(user, div_stra);
            List<RSMovie> compensatedTestMovieSet = null;
            if(isCompensate){
                compensatedTestMovieSet = ratingService.getLikeMovieByUserWithTestSet_Compensated(
                        user,
                        div_stra,
                        userMap.get(user.getId()).get(1),
                        compensateMoviesMap.get(user.getId())
                        );
            }
            for(int i : algs){
                RSGenLog genLog = new RSGenLog(
                        userResMap.get(user.getId()).get(i),
                        user, userMap.get(user.getId()).get(i), ((i != 1 && isCompensate)?compensatedTestMovieSet:testMovieSet));
                logTeam.addLog(genLog);
            }
        }
        //让GenLogTeam狠狠的爆金币然后存到数据库
        logService.addLogTeam(logTeam.createLogTeamForSQL());
        logService.addLogs(logTeam.createLogForSQL());

        System.out.println("Step5 Finish");

        //Step6.向前端返回电影数据
        if(users.size() == 1){
            List<String> rsNames = movieService.getNamesFromMWR(resMovies);
            List<Object> res = new Stack<>();
            res.add(resMovies);
            res.add(rsNames);
            return ServerResponse.createRespBySuccess(res);
        }else{
            return ServerResponse.createRespBySuccess("推荐结果已保存，请移步管理员【算法可视化】页面进行结果浏览");
        }

    }
}
