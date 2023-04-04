package com.gugusb.rsproject.algorithm;

import com.gugusb.rsproject.div_strategy.BaseStraPlus;
import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.entity.RSRating;
import com.gugusb.rsproject.entity.RSUser;
import com.gugusb.rsproject.util.ConstUtil;
import com.gugusb.rsproject.util.MovieWithRate;
import com.gugusb.rsproject.util.ResultEvaluation;

import java.util.*;

public class CB_Alg implements BaseAlg{

    private RSUser user = null;
    private Long fix_time;
    private BaseStraPlus divStra;
    private List<MovieWithRate> resultMovies;
    private Map<Integer, List<Integer>> baseMovies;
    private Map<Integer, RSRating> baseRatings;
    private List<Double> genreSimList;
    private List<Double> avgRateList;

    private Map<Integer, List<Integer>> allMovies;

    /**
     * cf alg
     *
     * @param user_   用户
     * @param movies  该用户打过分数的电影
     * @param ratings 该用户打过的分数评级
     */
    public CB_Alg(RSUser user_, Map<Integer, List<Integer>> movies, Map<Integer, RSRating> ratings, Map<Integer, List<Integer>> allMovies, BaseStraPlus divStra){
        this.user = user_;
        this.baseMovies = movies;
        this.baseRatings = ratings;
        this.allMovies = allMovies;
        this.avgRateList = new ArrayList<>();
        this.genreSimList = new ArrayList<>();
        this.divStra = divStra;
        this.fix_time = 0L;
        for(int i : ratings.keySet()){
            if(ratings.get(i).getTimestamp() > fix_time){
                this.fix_time = ratings.get(i).getTimestamp();
                System.out.println("最终操作时间：" + fix_time);
            }
        }
        genreSimList.add(0.0);
        avgRateList.add(0.0);
        for(int i = 1;i <= 19;i ++){
            genreSimList.add(getGenreSim(i));
            avgRateList.add(getGenreAvgRate(i));
        }
    }

    public List<Integer> getGenreListById(int movieId){
        return this.allMovies.get(movieId);
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
            s *= 1.0 / genres.get(0);
            ///加入兴趣时间衰减
            double time_rate = Math.pow(ConstUtil.TIME_A, ((1.0 * (fix_time - (baseRatings.get(i)).getTimestamp())) / ConstUtil.INTEREST_CHANGE_TIME));
            System.out.println("Genreno:" + genre_no + " timescore:" + time_rate + " before:" + s);
            s *= time_rate;
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
    public double getMovieRateWithUser(List<Integer> movieGenre){
        double fz = 0.0;
        double fm = 0.0;

        for(int i = 1;i <= 19;i ++){
            if(movieGenre.get(i) == 0){
                continue;
            }else{
                fz += 1.0 * genreSimList.get(i) * avgRateList.get(i);
                fm += 1.0 * genreSimList.get(i);
            }
        }

        if(Double.isNaN(fz / fm))
            return 0.0;
        return fz / fm;
    }

    @Override
    public List<MovieWithRate> getRecommandMovie() {
        List<MovieWithRate> list = new ArrayList<>();
        for(int i : allMovies.keySet()){
            //排除已经看过的电影
            if(divStra != null){

            }
            list.add(new MovieWithRate(i, getMovieRateWithUser(allMovies.get(i))));
        }
        Collections.sort(list);
        this.resultMovies = new ArrayList<>();
        for(MovieWithRate movie : list){
            if(movie.getRate() >= ConstUtil.RECOMMAND_LINE_CB){
                this.resultMovies.add(movie);
            }
        }
        if(this.resultMovies.size() < ConstUtil.RECOMMAND_COUNT_CB){
            this.resultMovies.addAll(list.subList(this.resultMovies.size(), ConstUtil.RECOMMAND_COUNT_CB));
        }
        return this.resultMovies;
    }

    @Override
    public double getRecall(List<RSMovie> movies) {
        return ResultEvaluation.getRecall(resultMovies, movies);
    }

    @Override
    public double getPrecision(List<RSMovie> movies) {
        return ResultEvaluation.getPrecision(resultMovies, movies);
    }

    @Override
    public double getAccuracy(List<RSMovie> movies) {
        return ResultEvaluation.getAccuracy(resultMovies, movies);
    }
}
