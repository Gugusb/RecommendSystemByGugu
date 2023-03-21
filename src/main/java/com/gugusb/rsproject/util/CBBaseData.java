package com.gugusb.rsproject.util;

import com.gugusb.rsproject.entity.RSRating;
import com.gugusb.rsproject.entity.RSUser;

import java.util.List;
import java.util.Map;

public class CBBaseData {
    private RSUser user_;
    private Map<Integer, List<Integer>> movies;
    private Map<Integer, RSRating> ratings;
    private Map<Integer, List<Integer>> allMovies;

    public CBBaseData(RSUser user, Map<Integer, List<Integer>> movies, Map<Integer, RSRating> ratings, Map<Integer, List<Integer>> allMovies){
        this.user_ = user;
        this.movies = movies;
        this.ratings = ratings;
        this.allMovies = allMovies;
    }

    public RSUser getUser_() {
        return user_;
    }

    public Map<Integer, List<Integer>> getMovies() {
        return movies;
    }

    public Map<Integer, RSRating> getRatings() {
        return ratings;
    }

    public Map<Integer, List<Integer>> getAllMovies() {
        return allMovies;
    }
}
