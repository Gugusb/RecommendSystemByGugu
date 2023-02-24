package com.gugusb.rsproject.util;

public class UserWithRate implements Comparable{

    private Integer userId;

    private Double rate;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer movieId) {
        this.userId = movieId;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public UserWithRate(int userId_, double rate_){
        this.userId = userId_;
        this.rate = rate_;
    }

    @Override
    public int compareTo(Object o) {
        return -1 * Double.compare(this.rate, ((UserWithRate)o).getRate());
    }
}
