package com.gugusb.rsproject.algorithm;

import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.entity.RSUser;
import com.gugusb.rsproject.util.ConstUtil;
import com.gugusb.rsproject.util.MovieWithRate;

import java.util.*;

public class ICF_Alg implements BaseAlg{

    private RSUser user = null;
    private int[][] rating_page;
    private int[][] co_matrix;
    private int[] spawn_count;
    private Set<Integer> like_movies;

    //STEP1.构建共现矩阵
    public ICF_Alg(int[][] rating_page, int[][] co_matrix, int[] spawn_count, RSUser user){
        this.rating_page = rating_page;
        this.co_matrix = co_matrix;
        this.spawn_count = spawn_count;
        this.user = user;
    }

    //计算两个物品的相似度
    public double getSimilarityBetweenMovies(int movieId1, int movieId2){
        double sim = 0.0;
        sim = co_matrix[movieId1][movieId2] / (Math.sqrt(spawn_count[movieId1] * spawn_count[movieId2]));
        return sim;
    }

    //获取用户可能感兴趣的所有物品
    public Set<Integer> getInterestMovies(){
        Set<Integer> integerList = new HashSet<>();
        //Step1.找到用户一定喜欢的电影表
        for(Integer i : this.getLikeMovies()){
            //Step2.遍历共现矩阵 找到用户喜欢的电影有关联的电影集合
            for(int j = 1;j < ConstUtil.MOVIE_COUNT;i ++){
                if(co_matrix[i][j] > 0){
                    integerList.add(j);
                }
            }
        }
        //Step3.再次得到的集合 排除用户已经观看过的电影
        for(int i = 1;i < ConstUtil.MOVIE_COUNT;i ++){
            if(rating_page[user.getId()][i] > 0 && integerList.contains(i)){
                integerList.remove(i);
            }
        }
        return integerList;
    }

    //获取用户一定喜欢的电影
    public Set<Integer> getLikeMovies(){
        if(like_movies != null){
            return like_movies;
        }
        like_movies = new HashSet<>();
        for(int i = 1;i < ConstUtil.MOVIE_COUNT;i ++) {
            if (rating_page[user.getId()][i] >= ConstUtil.LIKE_LINE) {
                like_movies.add(i);
            }
        }
        return like_movies;
    }

    //计算用户对物品的兴趣度
    public double getInterestToMovie(int movieId){
        double its = 0.0;
        //Step1.遍历用户喜欢看的电影 筛选和目标电影相关的影片
        for(Integer liked_movie : this.getLikeMovies()){
            if(co_matrix[liked_movie][movieId] > 0){
                //Step2.积累对电影的兴趣值
                its += getSimilarityBetweenMovies(liked_movie, movieId) * rating_page[user.getId()][liked_movie];
            }
        }
        return its;
    }


    @Override
    public List<MovieWithRate> getRecommandMovie() {
        List<MovieWithRate> movieWithRateList = new ArrayList<>();
        //Step1.获取所有用户可能感兴趣的电影
        for(Integer movieId : this.getInterestMovies()){
            //Step2.计算该电影的兴趣评分
            movieWithRateList.add(new MovieWithRate(movieId, getInterestToMovie(movieId)));
        }
        //Step3.对所有电影进行评分排序
        Collections.sort(movieWithRateList);
        return movieWithRateList;
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
