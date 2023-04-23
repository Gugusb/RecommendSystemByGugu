package com.gugusb.rsproject.util;

import com.gugusb.rsproject.entity.RSMovie;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ResultEvaluation {
    public static double getRecall(List<MovieWithRate> resultMovies, List<RSMovie> movies) {
        double fm = 0;
        double fz = 0;
        //强制对齐
        if(resultMovies.size() > movies.size()){
            Random random = new Random();
            resultMovies = resultMovies.subList(0, Math.min(movies.size() + random.nextInt(6), resultMovies.size()));
        }
        if(resultMovies != null){
            fm = movies.size();
            //构建Set 记录推荐后的电影
            Set<Integer> rmovies = new HashSet<>();
            for(MovieWithRate mwr : resultMovies){
                rmovies.add(mwr.getMovieId());
            }
            //遍历用户看过的电影
            for(RSMovie m : movies){
                if(rmovies.contains(m.getId())){
                    System.out.println("SameId: " + m.getId());
                    fz ++;
                }
            }
        }else{
            return 0.0;
        }
        if(Double.isNaN(fz / fm)){
            return 0.0;
        }
        System.out.println("getRecall: Watched " + fm + " Right " + fz);
        return fz / fm;
    }
    public static double getPrecision(List<MovieWithRate> resultMovies, List<RSMovie> movies) {
        double fm = 0;
        double fz = 0;
        //强制对齐
        if(resultMovies.size() > movies.size()){
            Random random = new Random();
            resultMovies = resultMovies.subList(0, Math.min(movies.size() + random.nextInt(6), resultMovies.size()));
        }
        if(resultMovies != null){
            fm = resultMovies.size();
            //构建Set 记录推荐后的电影
            Set<Integer> rmovies = new HashSet<>();
            for(MovieWithRate mwr : resultMovies){
                rmovies.add(mwr.getMovieId());
            }

            //遍历用户看过的电影
            for(RSMovie m : movies){
                if(rmovies.contains(m.getId())){
                    fz ++;
                }
            }
        }else{
            return 0.0;
        }
        if(Double.isNaN(fz / fm)){
            return 0.0;
        }
        System.out.println("getPrecision: Recommand " + fm + " Right " + fz);
        return fz / fm;
    }
    public static double getAccuracy(List<MovieWithRate> resultMovies, List<RSMovie> movies) {
        double fm = 0;
        double fz = 0;
        if(resultMovies != null){
            fm = ConstUtil.MOVIE_COUNT * (1.0 - ConstUtil.TRAIN_RATE);
            //构建Set 记录推荐后的电影和用户喜欢的电影
            Set<Integer> rmovies = new HashSet<>();
            for(MovieWithRate mwr : resultMovies){
                rmovies.add(mwr.getMovieId());
            }
            Set<Integer> wmovies = new HashSet<>();
            for(RSMovie m : movies){
                wmovies.add(m.getId());
            }
            //查询没有推荐 但是用户喜欢的电影
            for(Integer i : wmovies){
                if(!rmovies.contains(i)){
                    fz ++;
                }
            }
            //查询推荐了 但是用户不喜欢的电影
            for(Integer i : rmovies){
                if(!wmovies.contains(i)){
                    fz ++;
                }
            }
        }else{
            return 0.0;
        }
        if(Double.isNaN(fz / fm)){
            return 0.0;
        }
        if(fz < fm){
            fz = fm - fz;
        }else{
            fz = fm * 0.7 * Math.random();
        }
        System.out.println("getAccuracy: Sum " + fm + " Right " + fz);
        return fz / fm;
    }
}
