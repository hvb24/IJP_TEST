package com.IJP.jobs.repository;

import com.IJP.jobs.entity.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity,Long> {
    List<ApplicationEntity> findByEmpId(String empId);
    List<ApplicationEntity> findByJobId(String jobId);
    void deleteByJobId(String jobId);
    ApplicationEntity findByEmpIdAndJobId(String empId, String jobId);
}
