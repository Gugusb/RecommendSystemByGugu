package com.gugusb.rsproject.repository;

import com.gugusb.rsproject.entity.RSRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RSRatingRepository extends JpaRepository<RSRating, Integer> {
    List<RSRating> findByMovieid(Integer movieid);

    List<RSRating> findByUserid(Integer userid);

    List<RSRating> findByUseridOrderByMovieid(Integer userid);

    List<RSRating> findByMovieidIn(Collection<Integer> movieids);

    Optional<RSRating> findByUseridAndMovieid(Integer userid, Integer movieid);

    @Transactional
    @Modifying
    @Query("delete from RSRating r where r.userid = ?1 and r.movieid = ?2")
    int deleteByUseridAndMovieid(Integer userid, Integer movieid);

}