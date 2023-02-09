package com.gugusb.rsproject.algorithm;

import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.entity.RSUser;
import com.gugusb.rsproject.util.MovieWithRate;

import java.util.List;
import java.util.Map;

public interface BaseAlg {
    RSUser user = null;

    List<MovieWithRate> getRecommandMovie(Map<Integer, List<Integer> > allMovies);

    float getRecall(List<RSMovie> movies);
    float getPrecision(List<RSMovie> movies);
    float getAccuracy(List<RSMovie> movies);
}
