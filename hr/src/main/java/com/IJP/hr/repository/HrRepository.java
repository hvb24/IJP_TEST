package com.IJP.hr.repository;

import com.IJP.hr.entity.HrEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HrRepository extends JpaRepository<HrEntity, Integer> {
    Optional<HrEntity> findByEmail(String email);
    HrEntity save(HrEntity hrEntity);
}

