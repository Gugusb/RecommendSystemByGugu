package com.gugusb.rsproject.service;

import com.gugusb.rsproject.algorithm.*;
import com.gugusb.rsproject.div_strategy.BaseStraPlus;
import com.gugusb.rsproject.div_strategy.HO_Stra;
import com.gugusb.rsproject.entity.RSRating;
import com.gugusb.rsproject.entity.RSUser;
import com.gugusb.rsproject.repository.RSGenresRepository;
import com.gugusb.rsproject.repository.RSMovieRepository;
import com.gugusb.rsproject.repository.RSRatingRepository;
import com.gugusb.rsproject.util.CBBaseData;
import com.gugusb.rsproject.util.ConstUtil;
import com.gugusb.rsproject.util.GenreTransformer;
import com.gugusb.rsproject.util.MovieWithRate;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AlgorithmService {
    @Autowired
    RSMovieRepository movieRepository;
    @Autowired
    RSRatingRepository ratingRepository;
    @Autowired
    RSGenresRepository genresRepository;

    Map<Integer, Set<Integer>> userToMovie;
    Map<Integer, Set<Integer>> movieToUser;

    public CB_Alg getCFAlg(RSUser user, Map<Integer, List<Integer>> movies, Map<Integer, RSRating> ratings, Map<Integer, List<Integer>> allMovies, BaseStraPlus divStra){
        CB_Alg cfAlg = new CB_Alg(user, movies, ratings, allMovies, divStra);
        return cfAlg;
    }

    public UCF_Alg getUCFAlg(int[][] rating_page, RSUser user){
        UCF_Alg ucf_alg = new UCF_Alg(rating_page, user);
        return ucf_alg;
    }

    public ICF_Alg getICFAlg(int[][] rating_page, int[][] co_matrix, int[] spawn_count, RSUser user){
        ICF_Alg icf_alg = new ICF_Alg(rating_page, co_matrix, spawn_count, user);
        return icf_alg;
    }

    public MixAlg_1 getMixAlg_1(int[][] rating_page, int[] spawn_count, BaseStraPlus divStra, RSUser user){
        MixAlg_1 mixAlg1;
        //Step1.使用UCF推算出初始推荐电影
        UCF_Alg ucf_alg = new UCF_Alg(rating_page, user);
        List<MovieWithRate> ucfMovie = ucf_alg.getRecommandMovie();
        //Step2.对目标电影进行共现矩阵构建并生成ICF对象
        //  Step2.1整合用户喜欢的电影和第一步推荐的电影
        for(int i = 1; i < ConstUtil.MOVIE_COUNT; i ++) {
            if (rating_page[user.getId()][i] >= ConstUtil.LIKE_LINE) {
                ucfMovie.add(new MovieWithRate(i, 5));
            }
        }
        //  Step2.2拿到2.1整合后列表的共现矩阵
        int[][] co_matrix = getCoMatrixByMovieList(ucfMovie);
        //  Step2.3使用共现矩阵初始化ICF算法
        ICF_Alg icf_alg = new ICF_Alg(rating_page, co_matrix, spawn_count, user);
        //Step3创建混合算法1
        mixAlg1 = new MixAlg_1(ucf_alg, icf_alg);
        return mixAlg1;
    }

    public MixAlg_2 getMixAlg_2(int[][] rating_page, HO_Stra ho_stra, Map<Integer, List<Integer>> allMovies, RSUser user){
        MixAlg_2 mixAlg2;
        //Step1.创建UCF 使用其筛选出和目标用户相似度最高的用户
        UCF_Alg ucf_alg = new UCF_Alg(rating_page, user);
        List<Integer> users = ucf_alg.getTopNSimilarUser().keySet().stream().toList();
        //Step2.使用拿到的用户表去拿到相应的数据以多次启动CB算法
        Map<Integer, CBBaseData> cb_datas = new HashMap<>();
        for(Integer userId : users){
            RSUser user1 = new RSUser();
            user1.setId(userId);
            cb_datas.put(userId, new CBBaseData(user1,
                    getRatedMovieByUserFromTrainSet(user1, ho_stra),
                    getRatingMapForUserFromTarinSet(user1, ho_stra),
                    allMovies));
        }
        //Step3.创建混合算法对象 将拿到的用户表数据传入构造方法
        mixAlg2 = new MixAlg_2(ucf_alg, rating_page, cb_datas, user);

        return mixAlg2;
    }

    //获取仅针对部分电影相关评价的共现矩阵
    public int[][] getCoMatrixByMovieList(List<MovieWithRate> movieList){
        int[][] cm = new int[ConstUtil.MOVIE_COUNT + 10][ConstUtil.MOVIE_COUNT + 10];
        //Step1.构建电影ID的Collection
        List<Integer> movieIds = new ArrayList<>();
        for(MovieWithRate mwr : movieList){
            if(movieIds.contains(mwr.getMovieId())){
                continue;
            }
            movieIds.add(mwr.getMovieId());
        }
        System.out.println("Step1 Finish");
        //Step2.通过sql语句搜索出所有相关的评价
        List<RSRating> ratingList = ratingRepository.findByMovieidIn(movieIds);
        Set<Integer> userList = new HashSet<>();
        System.out.println("Step2 Finish");
        //Step3.构建评价矩阵+用户set
        int[][] ratingMatrix = new int[ConstUtil.USER_COUNT + 5][ConstUtil.MOVIE_COUNT + 5];
        for(RSRating rating : ratingList){
            ratingMatrix[rating.getUserid()][rating.getMovieid()] = rating.getRating();
            userList.add(rating.getUserid());
        }
        System.out.println("Step3 Finish");
        //Step4.构建共现矩阵
        for(int i = 0;i < movieIds.size();i ++){
            for(int j = i + 1;j < movieIds.size();j ++){
                for(Integer userId : userList){
                    if(ratingMatrix[userId][movieIds.get(i)] > 0 && ratingMatrix[userId][movieIds.get(j)] > 0){
                        //System.out.println("movie:" + movieIds.get(i) + " and movie:" + movieIds.get(j) + " coappear with " + userId);
                        cm[movieIds.get(i)][movieIds.get(j)] += 1;
                        cm[movieIds.get(j)][movieIds.get(i)] += 1;
                    }
                }
            }
        }
        System.out.println("Step4 Finish");
        return cm;
    }

    public List<RSRating> getRatingListByUser(RSUser user){
        List<RSRating> ratingList = ratingRepository.findByUserid(user.getId());
        return ratingList;
    }

    public Map<Integer, RSRating> getRatingMapForUserFromTarinSet(RSUser user, BaseStraPlus divStra){
        List<RSRating> ratingList = ratingRepository.findByUserid(user.getId());
        Map<Integer, RSRating> ratingMap = new HashMap<>();
        for (RSRating rating : ratingList){
            if(divStra.isTrainSet(rating.getId())){
                ratingMap.put(rating.getMovieid(), rating);
            }else{
                //System.out.println("因为数据集筛选而剔除了评价样例" + rating.getId());
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
                //System.out.println("因为数据集筛选而剔除了电影样例" + rating.getId());
            }
        }
        return map;
    }

    //构建从用户到电影 从电影到用户的字典
    private void InitMaps(){
        userToMovie = new HashMap<>();
        movieToUser = new HashMap<>();
        for(RSRating rating : ratingRepository.findAll()){
            int userId = rating.getUserid();
            int movieId = rating.getMovieid();
            if(userToMovie.containsKey(userId)){
                userToMovie.get(userId).add(movieId);
            }else{
                Set<Integer> newSet = new HashSet<>();
                newSet.add(movieId);
                userToMovie.put(userId, newSet);
            }

            if(movieToUser.containsKey(movieId)){
                movieToUser.get(movieId).add(userId);
            }else{
                Set<Integer> newSet = new HashSet<>();
                newSet.add(userId);
                movieToUser.put(movieId, newSet);
            }
        }
    }
    public Map<Integer, Set<Integer>> getUserToMovie(){
        if(userToMovie == null){
            InitMaps();
        }
        return userToMovie;
    }
    public Map<Integer, Set<Integer>> getMovieToUser(){
        if(movieToUser == null){
            InitMaps();
        }
        return movieToUser;
    }

    //给所有的用户赋初始权
    public double[] empowerUsers(){
        long ttime = ratingRepository.count();
        double[] utime = new double[ConstUtil.USER_COUNT + 100];
        for(int i : getUserToMovie().keySet()){
            utime[i] = 1.0 * getUserToMovie().get(i).size() / ttime;
        }
        return utime;
    }
    //给所有的电影赋初始权
    public double[] empowerMovies(){
        long ttime = ratingRepository.count();
        double[] mtime = new double[ConstUtil.MOVIE_COUNT + 100];
        for(int i : getMovieToUser().keySet()){
            mtime[i] = 1.0 * getMovieToUser().get(i).size() / ttime;
        }
        return mtime;
    }

    //获取初始化（经过PageRank）的电影权值
    public double[] getInitMoviePower(){

        double[] upowers = empowerUsers();
        double[] mpowers = empowerMovies();
        double m_sum = 0.0;

        boolean swc = true;
        int counter = 1;
        //激情迭代！
        while(swc){
            System.out.println("Time:" + counter);
            m_sum = 0.0;
            //迭代用户权值
            for(int i : getMovieToUser().keySet()){
                for(int j : getMovieToUser().get(i)){
                    upowers[j] += mpowers[i] / getMovieToUser().get(i).size();
                }
            }
            //迭代电影权值
            for(int i : getUserToMovie().keySet()){
                for(int j : getUserToMovie().get(i)){
                    mpowers[j] += upowers[i] / getUserToMovie().get(i).size();
                }
            }
            for(double i : mpowers){
                m_sum += i;
            }
            System.out.println("Movies");
            System.out.println("1:" + (mpowers[1] / m_sum) * 10000 + "%%");
            System.out.println("2:" + (mpowers[2] / m_sum) * 10000 + "%%");
            System.out.println("3:" + (mpowers[3] / m_sum) * 10000 + "%%");
            counter ++;
            if(counter > 100){
                swc = false;
            }
        }
        return mpowers;
    }
}
