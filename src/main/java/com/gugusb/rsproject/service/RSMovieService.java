package com.gugusb.rsproject.service;

import com.gugusb.rsproject.entity.MovieWithGenres;
import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.entity.RSRating;
import com.gugusb.rsproject.entity.RSUser;
import com.gugusb.rsproject.repository.RSMovieRepository;
import com.gugusb.rsproject.util.GenreTransformer;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RSMovieService {
    @Autowired
    RSMovieRepository movieRepository;

    public RSMovie getMovieById(Integer movieId){
        return movieRepository.findById(movieId).get();
    }
}
