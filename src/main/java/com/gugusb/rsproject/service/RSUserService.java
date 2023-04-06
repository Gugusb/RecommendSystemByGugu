package com.gugusb.rsproject.service;

import com.gugusb.rsproject.entity.RSUser;
import com.gugusb.rsproject.repository.RSUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RSUserService {
    @Autowired
    RSUserRepository userRepository;
}
