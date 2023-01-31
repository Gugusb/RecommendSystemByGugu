package com.gugusb.rsproject.algorithm;

import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.entity.RSUser;

import java.util.List;

public interface BaseAlg {
    RSUser user = null;

    List<RSMovie> getRecommandMovie();

    float getRecall(List<RSMovie> movies);
    float getPrecision(List<RSMovie> movies);
    float getAccuracy(List<RSMovie> movies);
}
