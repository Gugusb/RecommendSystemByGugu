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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
