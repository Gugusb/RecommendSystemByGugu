package com.gugusb.rsproject.service;

import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.entity.RSRating;
import com.gugusb.rsproject.entity.RSUser;
import com.gugusb.rsproject.repository.RSGenresRepository;
import com.gugusb.rsproject.repository.RSRatingRepository;
import com.gugusb.rsproject.util.GenreTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RSRatingService {
    @Autowired
    RSRatingRepository ratingRepository;

    @Autowired
    RSGenresRepository genresRepository;

    public List<RSRating> getRatingListByMovie(RSMovie movie){
        List<RSRating> ratingList = ratingRepository.findByMovieid(movie.getId());
        return ratingList;
    }

    public List<RSRating> getRatingListByUser(RSUser user){
        List<RSRating> ratingList = ratingRepository.findByUserid(user.getId());
        return ratingList;
    }

    public Map<Integer, RSRating> getRatingMapForMovie(RSMovie movie){
        List<RSRating> ratingList = ratingRepository.findByMovieid(movie.getId());
        Map<Integer, RSRating> ratingMap = new HashMap<>();
        for (RSRating rating : ratingList){
            ratingMap.put(rating.getUserid(), rating);
        }
        return ratingMap;
    }

    public Map<Integer, RSRating> getRatingMapForUser(RSUser user){
        List<RSRating> ratingList = ratingRepository.findByUserid(user.getId());
        Map<Integer, RSRating> ratingMap = new HashMap<>();
        for (RSRating rating : ratingList){
            ratingMap.put(rating.getMovieid(), rating);
        }
        return ratingMap;
    }

    public Map<Integer, List<Integer>> getRatedMovieByUser(RSUser user){
        Map<Integer, List<Integer>> map = new HashMap<>();
        for(RSRating rating : this.getRatingListByUser(user)){
            int movieId = rating.getMovieid();
            map.put(movieId, GenreTransformer.TransformGenres(genresRepository.findById(movieId).get()));
        }
        return map;
    }

}
