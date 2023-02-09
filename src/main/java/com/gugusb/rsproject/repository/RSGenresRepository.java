package com.gugusb.rsproject.repository;

import com.gugusb.rsproject.entity.MovieWithGenres;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RSGenresRepository extends JpaRepository<MovieWithGenres, Integer> {
}