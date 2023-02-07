package com.gugusb.rsproject.service;

import com.gugusb.rsproject.entity.MovieWithGeners;
import com.gugusb.rsproject.repository.GenersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RSGenerService {
    @Autowired
    GenersRepository genersRepository;


}
