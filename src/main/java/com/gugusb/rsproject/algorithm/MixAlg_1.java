package com.gugusb.rsproject.algorithm;

import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.entity.RSUser;
import com.gugusb.rsproject.util.ConstUtil;
import com.gugusb.rsproject.util.MovieWithRate;
import com.gugusb.rsproject.util.ResultEvaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

//瀑布式混合算法
public class MixAlg_1 implements BaseAlg{
    ICF_Alg icf_alg;
    UCF_Alg ucf_alg;
    private List<MovieWithRate> resultMovies;

    public MixAlg_1(UCF_Alg ucf_alg, ICF_Alg icf_alg){
        this.ucf_alg = ucf_alg;
        this.icf_alg = icf_alg;
    }
    @Override
    public List<MovieWithRate> getRecommandMovie() {
        List<MovieWithRate> movieWithRateList = new ArrayList<>();
        //Step1.获取所有用户可能感兴趣的电影
        for(MovieWithRate movie : ucf_alg.getRecommandMovie()){
            //Step2.计算该电影的兴趣评分
            int movieId = movie.getMovieId();
            movieWithRateList.add(new MovieWithRate(movieId, icf_alg.getInterestToMovie(movieId)));
        }
        //Step3.对所有电影进行评分排序
        Collections.sort(movieWithRateList);
        this.resultMovies = new ArrayList<>();
        for(MovieWithRate movie : movieWithRateList){
            if(movie.getRate() >= ConstUtil.RECOMMAND_LINE_M1){
                this.resultMovies.add(movie);
            }
        }
        if(this.resultMovies.size() < ConstUtil.RECOMMAND_COUNT_M1){
            this.resultMovies.addAll(movieWithRateList.subList(this.resultMovies.size(), ConstUtil.RECOMMAND_COUNT));
        }
        return this.resultMovies;
    }

    @Override
    public double getRecall(List<RSMovie> movies) {
        return ResultEvaluation.getRecall(resultMovies, movies);
    }

    @Override
    public double getPrecision(List<RSMovie> movies) {
        return ResultEvaluation.getPrecision(resultMovies, movies);
    }

    @Override
    public double getAccuracy(List<RSMovie> movies) {
        return ResultEvaluation.getAccuracy(resultMovies, movies);
    }
}
