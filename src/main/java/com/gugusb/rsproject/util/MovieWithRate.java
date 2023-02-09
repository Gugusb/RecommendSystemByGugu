package com.gugusb.rsproject.util;

public class MovieWithRate implements Comparable{

    private Integer movieId;

    private Double rate;

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public MovieWithRate(int movieId_, double rate_){
        this.movieId = movieId_;
        this.rate = rate_;
    }

    @Override
    public int compareTo(Object o) {
        return -1 * Double.compare(this.rate, ((MovieWithRate)o).getRate());
    }
}
