package com.IJP.jobs.services;


import com.IJP.jobs.entity.ApplicationEntity;
import com.IJP.jobs.entity.JobEntity;

import java.util.List;

public interface JobService {
//    JobEntity appliedjob(String job_id, String emp_id);

    List<JobEntity> getAllJobs();
    JobEntity getJobById(String job_id);
    JobEntity postJob(JobEntity jobEntity, String hrId);
    List<ApplicationEntity> getApplicationsByEmpId(String emp_id);
    boolean applyToJob(String jobId, String empId);
    List<ApplicationEntity> getApplicationsByJobId(String jobId);
    boolean deleteJobByJobId(String jobId);
    List<JobEntity> getJobByHrId(String hr_id);
}
