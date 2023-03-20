package com.gugusb.rsproject.repository;

import com.gugusb.rsproject.entity.RSRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface RSRatingRepository extends JpaRepository<RSRating, Integer> {
    List<RSRating> findByMovieid(Integer movieid);

    List<RSRating> findByUserid(Integer userid);

    List<RSRating> findByUseridOrderByMovieid(Integer userid);

    List<RSRating> findByMovieidIn(Collection<Integer> movieids);


}