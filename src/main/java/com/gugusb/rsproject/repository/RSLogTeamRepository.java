package com.gugusb.rsproject.repository;

import com.gugusb.rsproject.entity.RSLog;
import com.gugusb.rsproject.entity.RSLogTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RSLogTeamRepository extends JpaRepository<RSLogTeam, Integer> {
    Page<RSLogTeam> findAll(Pageable pageable);

}