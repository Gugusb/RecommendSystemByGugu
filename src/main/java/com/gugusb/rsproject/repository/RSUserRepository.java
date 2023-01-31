package com.gugusb.rsproject.repository;

import com.gugusb.rsproject.entity.RSUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RSUserRepository extends JpaRepository<RSUser, Integer> {
    Optional<RSUser> findById(Integer integer);
}