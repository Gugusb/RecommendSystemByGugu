package com.gugusb.rsproject.repository;

import com.gugusb.rsproject.entity.RSRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RSRatingRepository extends JpaRepository<RSRating, Integer> {
    List<RSRating> findByMovieid(Integer movieid);

    List<RSRating> findByUserid(Integer userid);
}