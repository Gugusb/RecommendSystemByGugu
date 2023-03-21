package com.gugusb.rsproject.algorithm;

import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.entity.RSRating;
import com.gugusb.rsproject.entity.RSUser;
import com.gugusb.rsproject.repository.RSRatingRepository;
import com.gugusb.rsproject.util.ConstUtil;
import com.gugusb.rsproject.util.MovieWithRate;
import com.gugusb.rsproject.util.UserWithRate;
import io.swagger.models.auth.In;
import org.hibernate.type.internal.ImmutableNamedBasicTypeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

public class UCF_Alg implements BaseAlg{

    private RSUser user = null;
    private int[][] rating_page;

    public double getSimilarityBetweenUser(Integer userId1, Integer userId2){
        double sim = 0.0;
        Set<Integer> t_movieid = new HashSet<>();
        List<Integer> vector_user1 = new ArrayList<>();
        List<Integer> vector_user2 = new ArrayList<>();
        //Step1.遍历电影记录 创建共同观影合集
        //Step2.创建两个用户的电影组向量
        for(int i = 1;i <= ConstUtil.MOVIE_COUNT;i ++){
            if(rating_page[userId1][i] + rating_page[userId2][i] > 0){
                t_movieid.add(i);
                vector_user1.add(rating_page[userId1][i]);
                vector_user2.add(rating_page[userId2][i]);
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
    public Map<Integer, Double> getTopNSimilarUser(){
        List<UserWithRate> userList = new ArrayList<>();
        for(int i = 1;i < ConstUtil.USER_COUNT;i ++){
            if(user.getId() == i){
                continue;
            }
            userList.add(new UserWithRate(i, getSimilarityBetweenUser(user.getId(), i)));
        }
        Collections.sort(userList);
        Map<Integer, Double> userMap = new HashMap<>();
        int count = 0;
        for(UserWithRate useri : userList){
            if(count < ConstUtil.UCF_USER_TOPN){
                count ++;
                userMap.put(useri.getUserId(), useri.getRate());
            }else break;
        }
        return userMap;
    }

    public List<MovieWithRate> getTopNSimilarMovie(){
        Map<Integer, Double> simUsers = getTopNSimilarUser();
        Set<Integer> movieIdList = new HashSet<>();
        //Step1.列出相似用户集最喜欢的几个电影
        for(Integer useri : simUsers.keySet()){
            List<MovieWithRate> tmpList = new ArrayList<>();
            //遍历该用户的观影历史
            for(int i = 1;i <= ConstUtil.MOVIE_COUNT;i ++){
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
                /*
                这里存在一些思想问题
                对于一个相似用户并没有看过这个电影
                那么其是否也成为推荐这个电影的影响因子
                一个想法是，这个用户是判定为和用户相似的用户，那么其所有的观影历史都是有价值的，故此以其评分平均值参与计算
                另一个想法是，这个用户既然没有看过，那么大可忽略其对该电影推荐的影响，分子和分母都不会因为其增加
                第二个想法显然是有些铸币的，要是只有一个相似用户看过电影，那么这个电影最终的推荐评分就只取决于这个人的评分
                那这显然是片面的捏
                 */
                if(rating_page[userId][i] != 0){
                    fz += rating_page[userId][i] * simUsers.get(userId);
                    fm += simUsers.get(userId);
                }else{
                    fz += ConstUtil.UCF_AVG_MOVIE_RATE * simUsers.get(userId);
                    fm += simUsers.get(userId);
                }
            }
            double dis = fz / fm;
            movieList.add(new MovieWithRate(i, dis));
        }
        Collections.sort(movieList);
        return movieList;
    }


    public UCF_Alg(int[][] rating_page, RSUser user){
        this.user = user;
        this.rating_page = rating_page;
    }

    @Override
    public List<MovieWithRate> getRecommandMovie() {
        return this.getTopNSimilarMovie();
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
    @Deprecated
    //不用构造函数传入参数的相似度匹配方法 涉及到大量IO 运行效率极低 故此已废弃
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
}
