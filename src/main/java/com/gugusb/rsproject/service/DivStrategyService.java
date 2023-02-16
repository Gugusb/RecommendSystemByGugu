package com.gugusb.rsproject.service;

import com.gugusb.rsproject.division_strategy.BaseStra;
import com.gugusb.rsproject.division_strategy.HO_Strategy;
import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.entity.RSRating;
import com.gugusb.rsproject.repository.RSMovieRepository;
import com.gugusb.rsproject.repository.RSRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DivStrategyService {
    @Autowired
    RSMovieRepository movieRepository;
    @Autowired
    RSRatingRepository ratingRepository;

    public BaseStra getDivStrategy(int divType){
        List<RSRating> ratings = ratingRepository.findAll();
        return switch (divType) {
            case 1 -> HO_Strategy.getHOInstance(ratings);
            default -> HO_Strategy.getHOInstance(ratings);
        };
    }

    public void refreshStrategy(BaseStra stra){
        stra.reGenerateSets();
    }
}
