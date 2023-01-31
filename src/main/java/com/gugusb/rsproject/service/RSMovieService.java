package com.gugusb.rsproject.service;

import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.repository.RSMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RSMovieService {
    @Autowired
    RSMovieRepository movieRepository;

    public List<RSMovie> findAll(){
        return movieRepository.findAll();
    }
}
