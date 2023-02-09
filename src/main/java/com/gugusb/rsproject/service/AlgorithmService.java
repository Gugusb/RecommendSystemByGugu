package com.gugusb.rsproject.service;

import com.gugusb.rsproject.algorithm.BaseAlg;
import com.gugusb.rsproject.algorithm.CF_Alg;
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

    public CF_Alg getCFAlg(RSUser user, Map<Integer, List<Integer>> movies, Map<Integer, RSRating> ratings){
        CF_Alg cfAlg = new CF_Alg(user, movies, ratings);

        return cfAlg;
    }


}
