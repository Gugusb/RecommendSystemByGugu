package com.gugusb.rsproject.division_strategy;

import com.gugusb.rsproject.entity.RSMovie;

import java.util.List;

public interface BaseStra {
    List<RSMovie> trainingSet = null;
    List<RSMovie> testSet = null;

    void initSets(List<RSMovie> init_movies);
    Boolean reGenerateSets();

    List<RSMovie> getTestSet();
    List<RSMovie> getTrainingSet();
}
