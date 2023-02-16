package com.gugusb.rsproject.div_strategy;

import com.gugusb.rsproject.entity.RSRating;

import java.util.List;
import java.util.Set;

public interface BaseStraPlus {
    Integer seed = null;

    Boolean isTestSet(Integer ratingId);
    Boolean isTrainSet(Integer ratingId);
}
