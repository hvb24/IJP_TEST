package com.IJP.jobs.repository;

import com.IJP.jobs.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<JobEntity,String>{
    List<JobEntity> findJobsByHrId(String hrId);

}
