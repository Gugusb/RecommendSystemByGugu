package com.gugusb.rsproject.controller;

import com.gugusb.rsproject.common.ServerResponse;
import com.gugusb.rsproject.entity.RSMovie;
import com.gugusb.rsproject.entity.RSRating;
import com.gugusb.rsproject.service.RSRatingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/ratings")
public class RatingController {

    @Autowired
    RSRatingService ratingService;

    @RequestMapping(value = "/get_ratings", method = RequestMethod.POST)
    public ServerResponse<Page<RSRating>> getRatings(HttpSession httpSession,
                                                     @RequestParam Integer tar_way,
                                                     @RequestParam Integer tar_id,
                                                     Pageable pageable){
        if(tar_way == -1){
            return ServerResponse.createRespBySuccess(ratingService.getAllRatings(pageable));
        }else{
            if(tar_id == null || tar_id == 0){
                return ServerResponse.createRespBySuccess(ratingService.getAllRatings(pageable));
            }
            if(tar_way == 1){
                return ServerResponse.createRespBySuccess(ratingService.getRatingsByMovieId(tar_id, pageable));
            }else{
                return ServerResponse.createRespBySuccess(ratingService.getRatingsByUserId(tar_id, pageable));
            }
        }
    }

    @RequestMapping(value = "/get_rating_count", method = RequestMethod.POST)
    public ServerResponse<Long> getRatingCount(HttpSession httpSession,
                                                     @RequestParam Integer tar_way,
                                                     @RequestParam Integer tar_id,
                                                     Pageable pageable){
        if(tar_way == -1){
            return ServerResponse.createRespBySuccess(ratingService.getAllRatings(pageable).getTotalElements());
        }else{
            if(tar_id == null || tar_id == 0){
                return ServerResponse.createRespBySuccess(ratingService.getAllRatings(pageable).getTotalElements());
            }
            if(tar_way == 1){
                return ServerResponse.createRespBySuccess(ratingService.getRatingsByMovieId(tar_id, pageable).getTotalElements());
            }else{
                return ServerResponse.createRespBySuccess(ratingService.getRatingsByUserId(tar_id, pageable).getTotalElements());
            }
        }
    }

    @RequestMapping(value = "/delete_rating", method = RequestMethod.POST)
    public ServerResponse<String> deleteRating(HttpSession httpSession,
                                             @RequestParam Integer ratingId){
        if(ratingService.deleteRating(ratingId)){
            return ServerResponse.createRespBySuccess("OK");
        }
        return ServerResponse.createByErrorMessage("No");
    }

    @RequestMapping(value = "/check_rating", method = RequestMethod.POST)
    public ServerResponse checkRating(HttpSession httpSession,
                                               @RequestParam Integer movieId){
       Object userId = httpSession.getAttribute("userId");
       if(userId == null){
           return ServerResponse.createRespBySuccess(-2);
       }else{
           Integer rat = ratingService.getRatingByTwoId((Integer) userId, movieId);
           return ServerResponse.createRespBySuccess(rat);
       }
    }
}
