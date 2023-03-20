package com.gugusb.rsproject.service;

import com.gugusb.rsproject.algorithm.CB_Alg;
import com.gugusb.rsproject.algorithm.ICF_Alg;
import com.gugusb.rsproject.algorithm.MixAlg_1;
import com.gugusb.rsproject.algorithm.UCF_Alg;
import com.gugusb.rsproject.entity.RSRating;
import com.gugusb.rsproject.entity.RSUser;
import com.gugusb.rsproject.repository.RSMovieRepository;
import com.gugusb.rsproject.repository.RSRatingRepository;
import com.gugusb.rsproject.util.ConstUtil;
import com.gugusb.rsproject.util.MovieWithRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AlgorithmService {
    @Autowired
    RSMovieRepository movieRepository;
    @Autowired
    RSRatingRepository ratingRepository;

    public CB_Alg getCFAlg(RSUser user, Map<Integer, List<Integer>> movies, Map<Integer, RSRating> ratings){
        CB_Alg cfAlg = new CB_Alg(user, movies, ratings);
        return cfAlg;
    }

    public UCF_Alg getUCFAlg(int[][] rating_page, RSUser user){
        UCF_Alg ucf_alg = new UCF_Alg(rating_page, user);
        return ucf_alg;
    }

    public ICF_Alg getICFAlg(int[][] rating_page, int[][] co_matrix, int[] spawn_count, RSUser user){
        ICF_Alg icf_alg = new ICF_Alg(rating_page, co_matrix, spawn_count, user);
        return icf_alg;
    }

    public MixAlg_1 getMixAlg_1(int[][] rating_page, int[] spawn_count, RSUser user){
        MixAlg_1 mixAlg1;
        //Step1.使用UCF推算出初始推荐电影
        UCF_Alg ucf_alg = new UCF_Alg(rating_page, user);
        List<MovieWithRate> ucfMovie = ucf_alg.getRecommandMovie(null);
        //Step2.对目标电影进行共现矩阵构建并生成ICF对象
        //  Step2.1整合用户喜欢的电影和第一步推荐的电影
        for(int i = 1; i < ConstUtil.MOVIE_COUNT; i ++) {
            if (rating_page[user.getId()][i] >= ConstUtil.LIKE_LINE) {
                ucfMovie.add(new MovieWithRate(i, 5));
            }
        }
        //  Step2.2拿到2.1整合后列表的共现矩阵
        int[][] co_matrix = getCoMatrixByMovieList(ucfMovie);
        //  Step2.3使用共现矩阵初始化ICF算法
        ICF_Alg icf_alg = new ICF_Alg(rating_page, co_matrix, spawn_count, user);
        //Step3创建混合算法1
        mixAlg1 = new MixAlg_1(ucf_alg, icf_alg);
        return mixAlg1;
    }

    //获取仅针对部分电影相关评价的共现矩阵
    public int[][] getCoMatrixByMovieList(List<MovieWithRate> movieList){
        int[][] cm = new int[ConstUtil.MOVIE_COUNT + 10][ConstUtil.MOVIE_COUNT + 10];
        //Step1.构建电影ID的Collection
        List<Integer> movieIds = new ArrayList<>();
        for(MovieWithRate mwr : movieList){
            if(movieIds.contains(mwr.getMovieId())){
                continue;
            }
            movieIds.add(mwr.getMovieId());
        }
        System.out.println("Step1 Finish");
        //Step2.通过sql语句搜索出所有相关的评价
        List<RSRating> ratingList = ratingRepository.findByMovieidIn(movieIds);
        Set<Integer> userList = new HashSet<>();
        System.out.println("Step2 Finish");
        //Step3.构建评价矩阵+用户set
        int[][] ratingMatrix = new int[ConstUtil.USER_COUNT + 5][ConstUtil.MOVIE_COUNT + 5];
        for(RSRating rating : ratingList){
            ratingMatrix[rating.getUserid()][rating.getMovieid()] = rating.getRating();
            userList.add(rating.getUserid());
        }
        System.out.println("Step3 Finish");
        //Step4.构建共现矩阵
        for(int i = 0;i < movieIds.size();i ++){
            for(int j = i + 1;j < movieIds.size();j ++){
                for(Integer userId : userList){
                    if(ratingMatrix[userId][movieIds.get(i)] > 0 && ratingMatrix[userId][movieIds.get(j)] > 0){
                        //System.out.println("movie:" + movieIds.get(i) + " and movie:" + movieIds.get(j) + " coappear with " + userId);
                        cm[movieIds.get(i)][movieIds.get(j)] += 1;
                        cm[movieIds.get(j)][movieIds.get(i)] += 1;
                    }
                }
            }
        }
        System.out.println("Step4 Finish");
        return cm;
    }


}
