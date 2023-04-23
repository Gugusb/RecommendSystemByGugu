package com.gugusb.rsproject.repository;

import com.gugusb.rsproject.entity.RSLog;
import com.gugusb.rsproject.entity.RSUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RSLogRepository extends JpaRepository<RSLog, Integer> {
    Optional<RSLog> findByUserid(Integer userid);

    List<RSLog> findByTeamid(Integer teamid);

    @Transactional
    @Modifying
    @Query("update RSLog r set r.recall = ?1 where r.userid = ?2")
    int updateRecallByUserid(Double recall, Integer userid);

}