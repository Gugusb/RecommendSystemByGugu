package com.gugusb.rsproject.logs;

import com.gugusb.rsproject.algorithm.BaseAlg;
import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.entity.RSUser;
import com.gugusb.rsproject.util.MovieWithRate;
import com.gugusb.rsproject.util.ResultEvaluation;

import java.sql.Timestamp;
import java.util.List;

public class RSGenLog {
    List<MovieWithRate> resultMovies;
    RSUser user;
    BaseAlg alg;
    Timestamp time;
    List<RSMovie> testMovieSet;

    public RSGenLog(List<MovieWithRate> resultMovies, RSUser user, BaseAlg alg, List<RSMovie> testMovieSet){
        this.resultMovies = resultMovies;
        this.user = user;
        this.alg = alg;
        this.time = new Timestamp(System.currentTimeMillis());
        this.testMovieSet = testMovieSet;
    }

    public List<MovieWithRate> getResultMovies() {
        return resultMovies;
    }
    public RSUser getUser() {
        return user;
    }
    public BaseAlg getAlg() {
        return alg;
    }
    public double getRecall() {
        return this.alg.getRecall(testMovieSet);
    }
    public double getPrecision() {
        return this.alg.getPrecision(testMovieSet);
    }
    public double getAccuracy() {
        return this.alg.getAccuracy(testMovieSet);
    }
}
