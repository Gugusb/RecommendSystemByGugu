package com.gugusb.rsproject.service;

import com.gugusb.rsproject.repository.RSUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RSUserInfService {
    @Autowired
    RSUserRepository userRepository;
    public String getGenderById(Integer id){
        return userRepository.findById(id).get().getGender();
    }
}
