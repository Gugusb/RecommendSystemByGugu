package com.gugusb.rsproject.algorithm;

import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.entity.RSRating;
import com.gugusb.rsproject.entity.RSUser;
import com.gugusb.rsproject.repository.RSRatingRepository;
import com.gugusb.rsproject.util.MovieWithRate;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

public class UCF_Alg implements BaseAlg{

    public double getSimilarityBetweenUser(List<RSRating> ratings_user1, List<RSRating> ratings_user2){
        double sim = 0f;
        Set<Integer> t_movieid = new HashSet<>();
        //Step1.遍历两个用户的电影记录 创建共同观影合集
        Map<Integer, Integer> movies_user1 = new HashMap<>();
        Map<Integer, Integer> movies_user2 = new HashMap<>();
        for(RSRating rating : ratings_user1){
            t_movieid.add(rating.getMovieid());
            movies_user1.put(rating.getMovieid(), rating.getRating());
        }
        for(RSRating rating : ratings_user2){
            t_movieid.add(rating.getMovieid());
            movies_user2.put(rating.getMovieid(), rating.getRating());
        }
        //Step2.创建两个用户的电影组向量
        List<Integer> vector_user1 = new ArrayList<>();
        List<Integer> vector_user2 = new ArrayList<>();
        for(int i : t_movieid){
            if(movies_user1.containsKey(i)){
                vector_user1.add(movies_user1.get(i));
            }else{
                vector_user1.add(0);
            }

            if(movies_user2.containsKey(i)){
                vector_user2.add(movies_user2.get(i));
            }else{
                vector_user2.add(0);
            }
        }
        //Step3.1.计算两个向量的余弦相似度
        double fz = 0.0;
        double fm1 = 0.0;
        double fm2 = 0.0;
        for(int i = 0;i < t_movieid.size();i ++){
            fz += vector_user1.get(i) * vector_user2.get(i);
            fm1 += vector_user1.get(i) * vector_user1.get(i);
            fm2 += vector_user2.get(i) * vector_user2.get(i);
        }
        //Step3.2.计算结果 查成分
        double fm = (Math.sqrt(fm1) * Math.sqrt(fm2));
        sim = fz / fm;
        if(Double.isNaN(sim)){
            return 0;
        }
        if(sim > 1.0){
            sim = 1.0;
        }

        return sim;
    }

    @Override
    public List<MovieWithRate> getRecommandMovie(Map<Integer, List<Integer>> allMovies) {
        return null;
    }

    @Override
    public float getRecall(List<RSMovie> movies) {
        return 0;
    }

    @Override
    public float getPrecision(List<RSMovie> movies) {
        return 0;
    }

    @Override
    public float getAccuracy(List<RSMovie> movies) {
        return 0;
    }
}
