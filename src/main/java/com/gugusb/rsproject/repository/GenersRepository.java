package com.gugusb.rsproject.repository;

import com.gugusb.rsproject.entity.MovieWithGeners;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenersRepository extends JpaRepository<MovieWithGeners, Integer> {
}