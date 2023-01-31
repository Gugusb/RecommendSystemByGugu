package com.gugusb.rsproject.division_strategy;

import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.util.ConstUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HO_Strategy implements BaseStra{
    private static HO_Strategy singleton = null;
    List<RSMovie> baseSet = null;
    List<RSMovie> trainingSet = null;
    List<RSMovie> testSet = null;

    private HO_Strategy(List<RSMovie> movies){
        initSets(movies);
        reGenerateSets();
    }

    public static synchronized BaseStra getHOInstance(List<RSMovie> movies) {
        if(singleton == null){
            singleton = new HO_Strategy(movies);
        }
        return singleton;
    }

    public void initSets(List<RSMovie> init_movies){
        //Init
        baseSet = new ArrayList<>();
        testSet = new ArrayList<>();
        trainingSet = new ArrayList<>();
        baseSet.addAll(init_movies);
    }

    @Override
    public Boolean reGenerateSets() {
        //检查初始化
        if(baseSet == null || testSet == null || trainingSet == null){
            return false;
        }
        //清空目标集合
        if(!testSet.isEmpty())
            testSet.clear();
        if(!trainingSet.isEmpty())
            trainingSet.clear();
        //遍历 划分测试集和训练集
        for (RSMovie movie : baseSet) {
            Random random = new Random();
            if(random.nextFloat() < ConstUtil.TO_RATE){
                trainingSet.add(movie);
            }else{
                testSet.add(movie);
            }
        }
        return true;
    }

    @Override
    public List<RSMovie> getTestSet() {
        return singleton.testSet;
    }

    @Override
    public List<RSMovie> getTrainingSet() {
        return singleton.trainingSet;
    }
}
