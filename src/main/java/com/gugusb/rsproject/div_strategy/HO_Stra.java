package com.gugusb.rsproject.div_strategy;

import com.gugusb.rsproject.util.ConstUtil;

import java.util.Random;

public class HO_Stra implements BaseStraPlus{
    Integer seed = null;
    double rate = 0.0;
    public HO_Stra(){
        Random random = new Random();
        this.seed = random.nextInt();
        this.rate = ConstUtil.TRAIN_RATE;
    }
    public HO_Stra(double rate){
        Random random = new Random();
        this.seed = random.nextInt();
        this.rate = rate;
    }
    public HO_Stra(Integer seed){
        Random random = new Random();
        this.seed = seed;
        this.rate = ConstUtil.TRAIN_RATE;
    }
    public HO_Stra(Integer seed, double rate){
        Random random = new Random();
        this.seed = seed;
        this.rate = rate;
    }
    @Override
    public Boolean isTestSet(Integer ratingId) {
        Random random = new Random(seed + ratingId);
        double r = random.nextDouble();
        double fr = r * ConstUtil.RANDOM_PRECISION - (int)(r * ConstUtil.RANDOM_PRECISION);
        return fr > this.rate;
    }

    @Override
    public Boolean isTrainSet(Integer ratingId) {
        Random random = new Random(seed + ratingId);
        double r = random.nextDouble();
        double fr = r * ConstUtil.RANDOM_PRECISION - (int)(r * ConstUtil.RANDOM_PRECISION);
        return fr <= this.rate;
    }
}
