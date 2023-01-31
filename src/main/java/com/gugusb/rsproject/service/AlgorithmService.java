package com.gugusb.rsproject.service;

import com.gugusb.rsproject.repository.RSMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlgorithmService {
    @Autowired
    RSMovieRepository movieRepository;


}
