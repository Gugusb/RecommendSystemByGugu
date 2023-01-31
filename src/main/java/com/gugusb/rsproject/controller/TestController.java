package com.gugusb.rsproject.controller;

import com.gugusb.rsproject.division_strategy.BaseStra;
import com.gugusb.rsproject.division_strategy.HO_Strategy;
import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.service.DivStrategyService;
import com.gugusb.rsproject.service.RSUserService;
import com.gugusb.rsproject.service.RSUserInfService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    @Autowired
    RSUserService userService;

    @Autowired
    RSUserInfService userInfService;
    @Autowired
    DivStrategyService divStrategyService;

    @RequestMapping(value = "/test/test", method = RequestMethod.POST)
    public String test(HttpSession httpSession, Integer id){
        return userInfService.getGenderById(id);
    }

    @RequestMapping(value = "/test/division", method = RequestMethod.POST)
    public String division(HttpSession httpSession){
        List<RSMovie> testmovies = divStrategyService.getDivStrategy(1).getTestSet();
        List<RSMovie> trainmovies = divStrategyService.getDivStrategy(1).getTrainingSet();
        divStrategyService.getDivStrategy(1).reGenerateSets();
        return "" + testmovies.size() + " " + trainmovies.size() + " " + (testmovies.size() + trainmovies.size());
    }

}
