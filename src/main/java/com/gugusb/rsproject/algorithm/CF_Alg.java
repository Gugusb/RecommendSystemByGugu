package com.gugusb.rsproject.algorithm;

import com.gugusb.rsproject.entity.MovieWithGeners;
import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.entity.RSRating;
import com.gugusb.rsproject.entity.RSUser;

import java.util.List;
import java.util.Map;

public class CF_Alg implements BaseAlg{

    private RSUser user = null;
    private Map<Integer, List<Integer>> baseMovies;
    private Map<Integer, RSRating> baseRatings;

    /**
     * cf alg
     *
     * @param user_   用户
     * @param movies  该用户打过分数的电影
     * @param ratings 该用户打过的分数评级
     */
    public CF_Alg(RSUser user_, Map<Integer, List<Integer>> movies, Map<Integer, RSRating> ratings){
        this.user = user_;
        this.baseMovies = movies;
        this.baseRatings = ratings;
    }

    /**
     * 计算用户对一个电影类型的喜好值
     *
     * @param genre_no 类型不
     * @return double
     */
    private double getGenreSim(int genre_no){
        double sim = 0.0;
        for(int i : baseMovies.keySet()){
            List<Integer> genres = baseMovies.get(i);
            if(genres.get(genre_no) == 0){
                continue;
            }
            double s = 1.0 * (baseRatings.get(i)).getRating();
            s /= 1.0 * genres.get(0);
            ///加入兴趣时间衰减
            sim += s;
        }
        return sim;
    }

    /**
     * 计算用户对一个类型的平均分
     *
     * @param genre_no 类型不
     * @return double
     */
    private double getGenreAvgRate(int genre_no){
        double sum = 0.0;
        int count = 0;
        for(int i : baseMovies.keySet()){
            List<Integer> genres = baseMovies.get(i);
            if(genres.get(genre_no) == 0){
                continue;
            }
            double s = 1.0 * (baseRatings.get(i)).getRating();
            sum += s;
            count ++;
        }
        double avg = sum / count;
        return avg;
    }

    /**
     * 计算用户与电影之间的距离Rate
     *
     * @param movieGenre 电影流派
     * @return double
     */
    private double getMovieRateWithUser(List<Integer> movieGenre){
        double fz = 0.0;
        double fm = 0.0;

        for(int i = 1;i <= 19;i ++){
            if(movieGenre.get(i) == 0){
                continue;
            }else{
                fz += 1.0 * getGenreSim(i) * getGenreAvgRate(i);
                fm += 1.0 * getGenreSim(i);
            }
        }

        if(fm == 0)
            return -1;
        return fz / fm;
    }

    @Override
    public List<RSMovie> getRecommandMovie() {
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
