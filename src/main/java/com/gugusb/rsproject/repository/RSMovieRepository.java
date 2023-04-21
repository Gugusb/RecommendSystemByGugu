package com.gugusb.rsproject.repository;

import com.gugusb.rsproject.entity.RSMovie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RSMovieRepository extends JpaRepository<RSMovie, Integer> {
    @Transactional
    @Modifying
    @Query("update RSMovie r set r.title = ?1 where r.id = ?2")
    void updateTitleById(String title, Integer id);

    @Transactional
    @Modifying
    @Query("update RSMovie r set r.genres = ?1 where r.id = ?2")
    void updateGenresById(String genres, Integer id);
    Page<RSMovie> findAll(Pageable pageable);

    Page<RSMovie> findById(Integer movieId, Pageable pageable);
}