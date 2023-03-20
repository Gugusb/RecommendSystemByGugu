package com.gugusb.rsproject.algorithm;

import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.util.MovieWithRate;

import java.util.List;
import java.util.Map;

//插入式混合算法
public class MixAlg_2 implements BaseAlg{
    @Override
    public List<MovieWithRate> getRecommandMovie(Map<Integer, List<Integer>> allMovies) {
        //Step1.使用UCF进行基础数据构建
        //Step2.使用CB得到的分数当作计算用户和电影距离时出现空白分数的代替
        //Step2.输出赋分排序后的电影条目
        return null;
    }

    @Override
    public float getRecall(List<RSMovie> movies) {
        return 0;
    }

    @Override
    public float getPrecision(List<RSMovie> movies) {
        return 0;
    }

    @Override
    public float getAccuracy(List<RSMovie> movies) {
        return 0;
    }
}
