package com.gugusb.rsproject.algorithm;

import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.util.MovieWithRate;

import java.util.List;
import java.util.Map;

//线性加权混合算法
public class MixAlg_3 implements BaseAlg{
    private List<MovieWithRate> resultMovies;
    @Override
    public List<MovieWithRate> getRecommandMovie() {
        //Step1.计算出CB得到的电影条目及赋分情况
        //Step2.计算出UCF得到的电影条目及赋分情况
        //Step3.对电影进行加权重新评分
        //Step4.输出最终的推荐列表
        return null;
    }

    @Override
    public double getRecall(List<RSMovie> movies) {
        return 0;
    }

    @Override
    public double getPrecision(List<RSMovie> movies) {
        return 0;
    }

    @Override
    public double getAccuracy(List<RSMovie> movies) {
        return 0;
    }
}
