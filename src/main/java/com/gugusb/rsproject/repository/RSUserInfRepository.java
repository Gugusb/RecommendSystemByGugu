package com.gugusb.rsproject.repository;

import com.gugusb.rsproject.entity.RSUserInf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RSUserInfRepository extends JpaRepository<RSUserInf, Integer> {
    @Override
    Optional<RSUserInf> findById(Integer integer);
}