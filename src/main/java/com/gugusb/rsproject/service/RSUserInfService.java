package com.gugusb.rsproject.service;

import com.gugusb.rsproject.entity.RSUser;
import com.gugusb.rsproject.entity.RSUserInf;
import com.gugusb.rsproject.repository.RSUserInfRepository;
import com.gugusb.rsproject.repository.RSUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RSUserInfService {
    @Autowired
    RSUserRepository userRepository;
    @Autowired
    RSUserInfRepository userInfRepository;
    public Long getUserCount(){
        return userInfRepository.count();
    }
    public String getGenderById(Integer id){
        return userRepository.findById(id).get().getGender();
    }

    public Boolean loginCheck(RSUserInf userInf){
        RSUserInf user = userInfRepository.findById(userInf.getId()).get();
        if(user.getPassword().equals(userInf.getPassword())){
            return true;
        }
        return false;
    }

    public Integer loginCheck(String name, String password){
        RSUserInf user = userInfRepository.findByName(name).get();
        if(user.getPassword().equals(password)){
            return user.getId();
        }
        return -1;
    }
    public Boolean existCheck(RSUser user){
        RSUser user1 = userRepository.findById(user.getId()).get();
        if(user1 != null){
            return true;
        }
        return false;
    }
    public Boolean existCheck(RSUserInf user){
        RSUserInf user1 = userInfRepository.findById(user.getId()).get();
        if(user1 != null){
            return true;
        }
        return false;
    }
    public Boolean updateUserInf(RSUserInf userInf, RSUser user){
        if(!existCheck(userInf)){
            return false;
        }
        if(userInf.getName() != null){
            userInfRepository.saveAndFlush(userInf);
        }
        if(userInf.getPassword() != null){
            userRepository.saveAndFlush(user);
        }
        return true;
    }
    public Boolean registerUser(RSUser user, RSUserInf userInf){
        userInfRepository.save(userInf);
        userRepository.save(user);
        return true;
    }

    public String getUserName(RSUser user){
        RSUserInf userInf = userInfRepository.findById(user.getId()).get();
        if(userInf == null){
            return "";
        }
        return  userInf.getName();
    }
}
