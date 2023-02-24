package com.gugusb.rsproject.service;

import com.gugusb.rsproject.div_strategy.BaseStraPlus;
import com.gugusb.rsproject.div_strategy.HO_Stra;
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
import java.util.Set;

@Service
public class RSRatingService {
    private int[][] rat_page;

    @Autowired
    RSRatingRepository ratingRepository;

    @Autowired
    RSGenresRepository genresRepository;

    public List<RSRating> getAllRatings(){
        return ratingRepository.findAll();
    }

    public int[][] getAllRatingPage(HO_Stra ho_stra){
        if(rat_page == null){
            rat_page = new int[6050][4000];
            for(RSRating i : ratingRepository.findAll()){
                //排除测试集数据
                if(ho_stra.isTestSet(i.getId())){
                    continue;
                }
                rat_page[i.getUserid()][i.getMovieid()] = i.getRating();
            }
        }
        return rat_page;
    }

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

    public Map<Integer, RSRating> getRatingMapForUserFromTarinSet(RSUser user, BaseStraPlus divStra){
        List<RSRating> ratingList = ratingRepository.findByUserid(user.getId());
        Map<Integer, RSRating> ratingMap = new HashMap<>();
        for (RSRating rating : ratingList){
            if(divStra.isTrainSet(rating.getId())){
                ratingMap.put(rating.getMovieid(), rating);
            }else{
                System.out.println("因为数据集筛选而剔除了评价样例" + rating.getId());
            }

        }
        return ratingMap;
    }

    public Map<Integer, List<Integer>> getRatedMovieByUserFromTrainSet(RSUser user, BaseStraPlus divStra){
        Map<Integer, List<Integer>> map = new HashMap<>();
        for(RSRating rating : this.getRatingListByUser(user)){
            if(divStra.isTrainSet(rating.getId())){
                int movieId = rating.getMovieid();
                map.put(movieId, GenreTransformer.TransformGenres(genresRepository.findById(movieId).get()));
            }else{
                System.out.println("因为数据集筛选而剔除了电影样例" + rating.getId());
            }
        }
        return map;
    }

}
