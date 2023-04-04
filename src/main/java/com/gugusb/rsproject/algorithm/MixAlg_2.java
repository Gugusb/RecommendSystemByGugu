package com.gugusb.rsproject.algorithm;

import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.entity.RSUser;
import com.gugusb.rsproject.util.CBBaseData;
import com.gugusb.rsproject.util.ConstUtil;
import com.gugusb.rsproject.util.MovieWithRate;
import com.gugusb.rsproject.util.ResultEvaluation;

import java.util.*;

//插入式混合算法
public class MixAlg_2 implements BaseAlg{
    RSUser user;
    private List<MovieWithRate> resultMovies;
    UCF_Alg ucf_alg;
    int[][] rating_page;
    Map<Integer, CBBaseData> cb_datas;

    public MixAlg_2(UCF_Alg ucf_alg, int[][] rating_page, Map<Integer, CBBaseData> cb_datas, RSUser user){
        this.user = user;
        this.ucf_alg = ucf_alg;
        this.rating_page = rating_page;
        this.cb_datas = cb_datas;
    }

    public List<MovieWithRate> getTopNSimilarMovieWithCB(){
        Map<Integer, Double> simUsers = ucf_alg.getTopNSimilarUser();
        Set<Integer> movieIdList = new HashSet<>();
        //Step1.列出相似用户集最喜欢的几个电影
        for(Integer useri : simUsers.keySet()){
            List<MovieWithRate> tmpList = new ArrayList<>();
            //遍历该用户的观影历史
            for(int i = 1; i <= ConstUtil.MOVIE_COUNT; i ++){
                if(rating_page[useri][i] != 0){
                    if(rating_page[this.user.getId()][i] == 0){
                        tmpList.add(new MovieWithRate(i, rating_page[useri][i]));
                    }
                }
            }
            Collections.sort(tmpList);
            int count = 0;
            for(MovieWithRate moviei : tmpList){
                if(count < ConstUtil.UCF_PER_MOVIE_TOPN){
                    count ++;
                    movieIdList.add(moviei.getMovieId());
                }else break;
            }
        }
        //Step2.分别计算所有电影和用户的距离
        List<MovieWithRate> movieList = new ArrayList<>();
        for(Integer i : movieIdList){
            double fz = 0.0;
            double fm = 0.0;
            for(Integer userId : simUsers.keySet()){
                if(rating_page[userId][i] != 0){
                    fz += rating_page[userId][i] * simUsers.get(userId);
                    fm += simUsers.get(userId);
                }else{
                    RSUser rsUser = new RSUser();
                    rsUser.setId(userId);
                    CB_Alg cb_alg = new CB_Alg(rsUser, cb_datas.get(userId).getMovies(), cb_datas.get(userId).getRatings(), cb_datas.get(userId).getAllMovies(), null);
                    fz += cb_alg.getMovieRateWithUser(cb_alg.getGenreListById(i)) * simUsers.get(userId);
                    fm += simUsers.get(userId);
                }
            }
            double dis = fz / fm;
            movieList.add(new MovieWithRate(i, dis));
        }
        Collections.sort(movieList);
        return movieList;
    }

    @Override
    public List<MovieWithRate> getRecommandMovie() {
        //Step1.使用UCF进行基础数据构建
        //Step2.使用CB得到的分数当作计算用户和电影距离时出现空白分数的代替
        //Step2.输出赋分排序后的电影条目
        List<MovieWithRate> temp = getTopNSimilarMovieWithCB();
        this.resultMovies = new ArrayList<>();
        for(MovieWithRate movie : temp){
            if(movie.getRate() >= ConstUtil.RECOMMAND_LINE_M2){
                this.resultMovies.add(movie);
            }
        }
        if(this.resultMovies.size() < ConstUtil.RECOMMAND_COUNT){
            this.resultMovies.addAll(temp.subList(this.resultMovies.size(), ConstUtil.RECOMMAND_COUNT));
        }
        return resultMovies;
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
