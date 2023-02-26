package com.gugusb.rsproject.division_strategy;

import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.entity.RSRating;

import javax.sql.RowSetInternal;
import java.util.List;
import java.util.Set;

@Deprecated
public interface BaseStra {
    List<RSRating> trainingSet = null;
    List<RSRating> testSet = null;

    void initSets(List<RSRating> init_movies);
    Boolean reGenerateSets();

    Set<RSRating> getTestSet();
    Set<RSRating> getTrainingSet();
    Set<Integer> getTestIdSet();
    Set<Integer> getTrainingIdSet();
}
