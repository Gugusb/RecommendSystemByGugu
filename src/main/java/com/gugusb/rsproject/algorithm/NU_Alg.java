package com.gugusb.rsproject.algorithm;

import com.gugusb.rsproject.div_strategy.BaseStraPlus;
import com.gugusb.rsproject.entity.RSRating;
import com.gugusb.rsproject.entity.RSUser;
import com.gugusb.rsproject.util.ConstUtil;

import java.util.List;
import java.util.Map;

public class NU_Alg extends CB_Alg{
    private List<Integer> userGenres;
    /**
     * cf alg
     *
     * @param user_     用户
     * @param movies    该用户打过分数的电影
     * @param ratings   该用户打过的分数评级
     * @param allMovies
     * @param divStra
     */
    public NU_Alg(RSUser user_, Map<Integer, List<Integer>> movies, Map<Integer, RSRating> ratings, Map<Integer, List<Integer>> allMovies, BaseStraPlus divStra, List<Integer> userGenres) {
        super(user_, movies, ratings, allMovies, divStra);
        this.userGenres = userGenres;

        super.genreSimList.clear();
        super.avgRateList.clear();
        super.genreSimList.add(0.0);
        super.avgRateList.add(0.0);
        for(int i = 1;i <= 18;i ++){
            genreSimList.add(this.getGenreSimC(i));
            avgRateList.add(this.getGenreAvgRateC(i));
        }
    }

    protected double getGenreSimC(int genre_no){
        if(userGenres.contains(genre_no)){
            return 1.0;
        }else{
            return 0.0;
        }
    }

    protected double getGenreAvgRateC(int genre_no){
        if(userGenres.contains(genre_no)){
            return 1.0;
        }else{
            return 0.0;
        }
    }
}
