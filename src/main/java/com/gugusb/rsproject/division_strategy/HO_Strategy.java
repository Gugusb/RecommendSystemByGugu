package com.gugusb.rsproject.division_strategy;

import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.entity.RSRating;
import com.gugusb.rsproject.util.ConstUtil;

import java.util.*;

public class HO_Strategy implements BaseStra{
    private static HO_Strategy singleton = null;
    List<RSRating> baseSet = null;
    Set<RSRating> trainingSet = null;
    Set<Integer> trainingIdSet = null;
    Set<RSRating> testSet = null;
    Set<Integer> testIdSet = null;

    private HO_Strategy(List<RSRating> ratings){
        initSets(ratings);
        reGenerateSets();
    }

    public static synchronized BaseStra getHOInstance(List<RSRating> ratings) {
        if(singleton == null){
            singleton = new HO_Strategy(ratings);
        }
        return singleton;
    }

    public void initSets(List<RSRating> init_movies){
        //Init
        baseSet = new ArrayList<>();
        testSet = new HashSet<>();
        trainingSet = new HashSet<>();
        testIdSet = new HashSet<>();
        trainingIdSet = new HashSet<>();
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
        for (RSRating rating : baseSet) {
            Random random = new Random();
            if(random.nextFloat() < ConstUtil.TRAIN_RATE){
                trainingSet.add(rating);
                trainingIdSet.add(rating.getId());
            }else{
                testSet.add(rating);
                testIdSet.add(rating.getId());
            }
        }
        return true;
    }

    @Override
    public Set<RSRating> getTestSet() {
        return singleton.testSet;
    }

    @Override
    public Set<RSRating> getTrainingSet() {
        return singleton.trainingSet;
    }

    @Override
    public Set<Integer> getTestIdSet() {
        return singleton.testIdSet;
    }

    @Override
    public Set<Integer> getTrainingIdSet() {
        return singleton.trainingIdSet;
    }
}
