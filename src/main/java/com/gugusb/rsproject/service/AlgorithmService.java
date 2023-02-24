package com.gugusb.rsproject.service;

import com.gugusb.rsproject.algorithm.CB_Alg;
import com.gugusb.rsproject.algorithm.UCF_Alg;
import com.gugusb.rsproject.entity.RSRating;
import com.gugusb.rsproject.entity.RSUser;
import com.gugusb.rsproject.repository.RSMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AlgorithmService {
    @Autowired
    RSMovieRepository movieRepository;

    public CB_Alg getCFAlg(RSUser user, Map<Integer, List<Integer>> movies, Map<Integer, RSRating> ratings){
        CB_Alg cfAlg = new CB_Alg(user, movies, ratings);

        return cfAlg;
    }

    public UCF_Alg getUCFAlg(int[][] rating_page, RSUser user){
        UCF_Alg ucf_alg = new UCF_Alg(rating_page, user);
        return ucf_alg;
    }


}
