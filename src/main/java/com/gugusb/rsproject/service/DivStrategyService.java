package com.gugusb.rsproject.service;

import com.gugusb.rsproject.division_strategy.BaseStra;
import com.gugusb.rsproject.division_strategy.HO_Strategy;
import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.repository.RSMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DivStrategyService {
    @Autowired
    RSMovieRepository movieRepository;

    public BaseStra getDivStrategy(int divType){
        List<RSMovie> movies = movieRepository.findAll();
        return switch (divType) {
            case 1 -> HO_Strategy.getHOInstance(movies);
            default -> HO_Strategy.getHOInstance(movies);
        };
    }

    public void refreshStrategy(BaseStra stra){
        stra.reGenerateSets();
    }
}
