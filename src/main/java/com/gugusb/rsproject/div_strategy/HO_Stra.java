package com.gugusb.rsproject.div_strategy;

import com.gugusb.rsproject.util.ConstUtil;

import java.util.Random;

public class HO_Stra implements BaseStraPlus{
    Integer seed = null;
    public HO_Stra(){
        Random random = new Random();
        this.seed = random.nextInt();
    }
    @Override
    public Boolean isTestSet(Integer ratingId) {
        Random random = new Random(seed + ratingId);
        double r = random.nextDouble();
        double fr = r * ConstUtil.RANDOM_PRECISION - (int)(r * ConstUtil.RANDOM_PRECISION);
        return fr > ConstUtil.TRAIN_RATE;
    }

    @Override
    public Boolean isTrainSet(Integer ratingId) {
        Random random = new Random(seed + ratingId);
        double r = random.nextDouble();
        double fr = r * ConstUtil.RANDOM_PRECISION - (int)(r * ConstUtil.RANDOM_PRECISION);
        return fr <= ConstUtil.TRAIN_RATE;
    }
}
