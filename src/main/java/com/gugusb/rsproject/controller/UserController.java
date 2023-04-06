package com.gugusb.rsproject.controller;

import com.gugusb.rsproject.common.ServerResponse;
import com.gugusb.rsproject.entity.RSUser;
import com.gugusb.rsproject.entity.RSUserInf;
import com.gugusb.rsproject.service.RSRatingService;
import com.gugusb.rsproject.service.RSUserInfService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/user")
public class UserController {
    @Autowired
    RSUserInfService userInfService;
    @Autowired
    RSRatingService ratingService;

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ServerResponse test(HttpSession httpSession, Integer[] arr){
        return ServerResponse.createRespBySuccess(arr[2]);
    }
    //基础操作
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ServerResponse userLogin(HttpSession httpSession, RSUserInf user){
        if(userInfService.loginCheck(user)){
            httpSession.setAttribute("userId", user.getId());
            return ServerResponse.createRespBySuccess("");
        }
        return ServerResponse.createByErrorMessage("Wrong Password Or Unexist User");
    }
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ServerResponse userRegister(HttpSession httpSession, RSUserInf userInf, RSUser user){

        if(userInfService.registerUser(user, userInf)){
            return ServerResponse.createRespBySuccess();
        }
        return ServerResponse.createByErrorMessage("注册失败");
    }
    //用户评分操作
    @RequestMapping(value = "/rating", method = RequestMethod.POST)
    public ServerResponse userCreateRating(HttpSession httpSession, Integer userid, Integer movieid, Integer rating){
        if(userid == null){
            userid = (Integer) httpSession.getAttribute("userId");
        }
        if(ratingService.addRating(userid, movieid, rating)){
            return ServerResponse.createRespBySuccess("Rating succeed");
        }
        return ServerResponse.createByErrorMessage("评分失败");
    }
    @RequestMapping(value = "/change_rating", method = RequestMethod.POST)
    public ServerResponse userChangeRating(HttpSession httpSession, Integer userid, Integer movieid, Integer rating){
        if(userid == null){
            userid = (Integer) httpSession.getAttribute("userId");
        }
        if(ratingService.updateRating(userid, movieid, rating)){
            return ServerResponse.createRespBySuccess("Rating succeed");
        }
        return ServerResponse.createByErrorMessage("评分失败");
    }
    @RequestMapping(value = "/delete_rating", method = RequestMethod.POST)
    public ServerResponse userDeleteRating(HttpSession httpSession, Integer ratingid, Integer userid, Integer movieid){
        if(ratingid != null){
            ratingService.deleteRatingById(ratingid);
            return ServerResponse.createRespBySuccess("Delete succeed");
        }
        if(userid == null){
            userid = (Integer) httpSession.getAttribute("userId");
        }
        if(ratingService.deleteratingByUserAndMovie(userid, movieid)){
            return ServerResponse.createRespBySuccess("Delete succeed");
        }
        return ServerResponse.createByErrorMessage("Delete失败");
    }
}
