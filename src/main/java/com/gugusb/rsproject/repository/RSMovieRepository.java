package com.gugusb.rsproject.repository;

import com.gugusb.rsproject.entity.RSMovie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RSMovieRepository extends JpaRepository<RSMovie, Integer> {
}