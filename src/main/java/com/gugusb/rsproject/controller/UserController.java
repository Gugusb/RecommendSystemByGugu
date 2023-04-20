package com.gugusb.rsproject.controller;
import org.springframework.data.domain.Page;
import com.gugusb.rsproject.common.ServerResponse;
import com.gugusb.rsproject.entity.RSRating;
import com.gugusb.rsproject.entity.RSUser;
import com.gugusb.rsproject.entity.RSUserInf;
import com.gugusb.rsproject.service.RSMovieService;
import com.gugusb.rsproject.service.RSRatingService;
import com.gugusb.rsproject.service.RSUserInfService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/user")
public class UserController {
    @Autowired
    RSUserInfService userInfService;
    @Autowired
    RSRatingService ratingService;
    @Autowired
    RSMovieService movieService;

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ServerResponse test(HttpSession httpSession, Integer[] arr){
        return ServerResponse.createRespBySuccess(arr[2]);
    }
    //基础操作
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ServerResponse userLogin(HttpSession httpSession, @RequestBody RSUserInf user){
        if(userInfService.loginCheck(user)){
            httpSession.setAttribute("userId", user.getId());
            httpSession.setMaxInactiveInterval(1000000086);
            System.out.println(httpSession.getAttribute("userId"));
            return ServerResponse.createRespBySuccess("");
        }
        return ServerResponse.createByErrorMessage("Wrong Password Or Unexist User");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ServerResponse userRegister(HttpSession httpSession, @RequestBody Map<String, Object> map){
        RSUserInf userInf = new RSUserInf();
        RSUser user = new RSUser();
        userInf.setName((String) map.get("name"));
        userInf.setPassword((String) map.get("password"));
        user.setAge(Integer.parseInt((String) map.get("age")));
        user.setGender("F");
        if((String)map.get("gender") == "0"){
            user.setGender("M");
        }
        user.setOccupation(Integer.parseInt((String) map.get("occupation")));
        user.setZipdode((String) map.get("zipnode"));
        System.out.println("reg" + map.get("name"));

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

    @RequestMapping(value = "/get_user_name", method = RequestMethod.POST)
    public ServerResponse getUserName(HttpSession httpSession){
        RSUser user = new RSUser();
        user.setId((Integer) httpSession.getAttribute("userId"));
        System.out.println(userInfService.getUserName(user));
        return ServerResponse.createRespBySuccess(userInfService.getUserName(user));
    }
    @RequestMapping(value = "/get_user_ratings", method = RequestMethod.POST)
    public ServerResponse getUserRatings(HttpSession httpSession,
                                         Pageable pageable){
        RSUser user = new RSUser();
        user.setId((Integer) httpSession.getAttribute("userId"));
        return ServerResponse.createRespBySuccess(ratingService.getRatingByUserIdPageable(user,pageable));
    }

    @RequestMapping(value = "/get_user_ratingnames", method = RequestMethod.POST)
    public ServerResponse getUserRatingNames(HttpSession httpSession,
                                             Pageable pageable){
        RSUser user = new RSUser();
        user.setId((Integer) httpSession.getAttribute("userId"));
        Page<RSRating> ratings = ratingService.getRatingByUserIdPageable(user,pageable);
        return ServerResponse.createRespBySuccess(movieService.getNamesFromRating(ratings));
    }

}
