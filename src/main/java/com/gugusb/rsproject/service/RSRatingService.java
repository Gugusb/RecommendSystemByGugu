package com.gugusb.rsproject.service;

import com.gugusb.rsproject.div_strategy.BaseStraPlus;
import com.gugusb.rsproject.div_strategy.HO_Stra;
import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.entity.RSRating;
import com.gugusb.rsproject.entity.RSUser;
import com.gugusb.rsproject.repository.RSGenresRepository;
import com.gugusb.rsproject.repository.RSRatingRepository;
import com.gugusb.rsproject.util.ConstUtil;
import com.gugusb.rsproject.util.GenreTransformer;
import com.gugusb.rsproject.util.MovieWithRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RSRatingService {

    private int[][] rat_page;
    private int[][] co_matrix;
    private int[] spawn_count;
    private BaseStraPlus least_stra;

    @Autowired
    RSRatingRepository ratingRepository;

    @Autowired
    RSGenresRepository genresRepository;

    public Boolean addRating(Integer userId, Integer movieId, Integer rating){
        if(ratingRepository.findByUseridAndMovieid(userId, movieId).isEmpty()){
            ratingRepository.save(new RSRating(userId, movieId, rating, System.currentTimeMillis()));
            return true;
        }
        return false;
    }
    public Boolean updateRating(Integer userId, Integer movieId, Integer rating){
        Optional<RSRating> rat = ratingRepository.findByUseridAndMovieid(userId, movieId);
        if(!ratingRepository.findByUseridAndMovieid(userId, movieId).isEmpty()){
            RSRating newrating = new RSRating(userId, movieId, rating, System.currentTimeMillis());
            newrating.setId(rat.get().getId());
            ratingRepository.save(newrating);
            return true;
        }
        return false;
    }
    public Boolean deleteRatingById(Integer ratingId){
        ratingRepository.deleteById(ratingId);
        return true;
    }
    public Boolean deleteratingByUserAndMovie(Integer userId, Integer movieId){
        Optional<RSRating> rating = ratingRepository.findByUseridAndMovieid(userId, movieId);
        if(!rating.isEmpty()){
            deleteRatingById(rating.get().getId());
            return true;
        }
        return false;
    }

    public List<RSRating> getAllRatings(){
        return ratingRepository.findAll();
    }

    public int[][] getAllRatingPage(HO_Stra ho_stra){
        if(rat_page == null || (rat_page != null && ho_stra != least_stra)){
            least_stra = ho_stra;
            rat_page = new int[ConstUtil.USER_COUNT + 10][ConstUtil.MOVIE_COUNT + 10];
            spawn_count = new int[ConstUtil.MOVIE_COUNT + 10];
            for(RSRating i : ratingRepository.findAll()){
                //排除测试集数据
                if(ho_stra.isTestSet(i.getId())){
                    continue;
                }
                if(i.getRating() >= ConstUtil.LIKE_LINE){
                    spawn_count[i.getMovieid()] ++;
                }
                rat_page[i.getUserid()][i.getMovieid()] = i.getRating();
            }
        }
        return rat_page;
    }

    //获取每个电影出现的次数 建议在生成RatingPage后使用
    public int[] getSpawnCount(HO_Stra ho_stra){
        if(spawn_count == null){
            for(RSRating i : ratingRepository.findAll()){
                //排除测试集数据
                if(ho_stra.isTestSet(i.getId())){
                    continue;
                }
                if(i.getRating() >= ConstUtil.LIKE_LINE){
                    spawn_count[i.getMovieid()] ++;
                }
            }
        }
        return spawn_count;
    }

    //获取所有电影评价的共现矩阵
    public int[][] getCoMatrix(HO_Stra ho_stra){
        if(co_matrix == null || (co_matrix != null && ho_stra != least_stra)){
            int[][] t_page = this.getAllRatingPage(ho_stra);
            co_matrix = new int[ConstUtil.MOVIE_COUNT + 10][ConstUtil.MOVIE_COUNT + 10];
            for(int movieid1 = 1;movieid1 < ConstUtil.MOVIE_COUNT + 10;movieid1 ++){
                for(int movieid2 = movieid1 + 1;movieid2 < ConstUtil.MOVIE_COUNT + 10;movieid2 ++){
                    for(int userid = 1;userid < ConstUtil.USER_COUNT + 10;userid ++){
                        if(t_page[userid][movieid1] > 0 && t_page[userid][movieid2] > 0){
                            co_matrix[movieid1][movieid2] ++;
                            co_matrix[movieid2][movieid1] ++;
                        }
                    }
                }
            }
        }
        return co_matrix;
    }

    //获取仅针对部分电影相关评价的共现矩阵
    public int[][] getCoMatrixByMovieList(List<MovieWithRate> movieList){
        int[][] cm = new int[ConstUtil.MOVIE_COUNT + 10][ConstUtil.MOVIE_COUNT + 10];
        //Step1.构建电影ID的Collection
        List<Integer> movieIds = new ArrayList<>();
        for(MovieWithRate mwr : movieList){
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
                        System.out.println("movie:" + movieIds.get(i) + " and movie:" + movieIds.get(j) + " coappear with " + userId);
                        cm[movieIds.get(i)][movieIds.get(j)] += 1;
                        cm[movieIds.get(j)][movieIds.get(i)] += 1;
                    }
                }
            }
        }
        System.out.println("Step4 Finish");
        return cm;
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
                System.out.println("[RatingMap]因为数据集筛选而剔除了评价样例" + rating.getId() + " 电影ID:" + rating.getMovieid());
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
                System.out.println("[MovieMap]因为数据集筛选而剔除了评价样例" + rating.getId() + " 电影ID:" + rating.getMovieid());
            }
        }
        return map;
    }

    public List<RSMovie> getLikeMovieByUserWithTestSet(RSUser user, BaseStraPlus divStra){
        List<RSMovie> ans = new ArrayList<>();
        for(RSRating rating : ratingRepository.findByUserid(user.getId())){
            if(rating.getRating() >= ConstUtil.LIKE_LINE_FOR_TEST && true){
                if(divStra.isTestSet(rating.getId())){
                    RSMovie movie = new RSMovie();
                    movie.setId(rating.getMovieid());
                    ans.add(movie);
                }
            }
        }
        return ans;
    }

}
