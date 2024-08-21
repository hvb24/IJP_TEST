package com.IJP.jobs.services.impl;


import com.IJP.jobs.entity.ApplicationEntity;
import com.IJP.jobs.entity.JobEntity;
import com.IJP.jobs.exceptions.JobsApiException;
import com.IJP.jobs.repository.ApplicationRepository;
import com.IJP.jobs.repository.JobRepository;
import com.IJP.jobs.services.JobService;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository Jobrepository;

    @Autowired
    private ApplicationRepository applicationRepository;


    @Override
    public JobEntity postJob(JobEntity jobEntity, String hrId) {
        try {
            if (hrId == null || hrId.isEmpty()|| hrId.length()==0) {
                throw new JobsApiException("HR ID is required", HttpStatus.BAD_REQUEST);
            }
            if (jobEntity == null) {
                throw new JobsApiException("Job details are required", HttpStatus.BAD_REQUEST);
            }

            jobEntity.setHrId(hrId); // Ensure HR ID is set

            System.out.println("inside the service impl: saving job with HR ID " + hrId);
            return Jobrepository.save(jobEntity);
        } catch (JobsApiException e) {
            // Rethrow the specific exception to be handled by the controller
            throw e;
        } catch (Exception e) {
            // Log unexpected exceptions
            System.out.println("Unexpected exception: " + e.getMessage());
            throw new JobsApiException("Job not created due to an unexpected error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @Override
    public List<JobEntity> getAllJobs() {
        try {
            List<JobEntity> jobs = Jobrepository.findAll();

            if (jobs.isEmpty()) {
                throw new JobsApiException("No jobs found", HttpStatus.NOT_FOUND);
            }

            return jobs;
        } catch (JobsApiException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("Unexpected exception while fetching jobs: " + e.getMessage());
            throw new JobsApiException("Error occurred while fetching jobs", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public JobEntity getJobById(String jobId) {
        try {
            return Jobrepository.findById(jobId)
                    .orElseThrow(() -> new JobsApiException("Job not found with ID: " + jobId, HttpStatus.NOT_FOUND));
        } catch (JobsApiException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("Unexpected exception while fetching job with ID " + jobId + ": " + e.getMessage());
            throw new JobsApiException("Error occurred while fetching job with ID: " + jobId, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<JobEntity> getJobByHrId(String hr_id) {
        try {
            if (hr_id == null || hr_id.isEmpty()) {
                throw new JobsApiException("HR ID is required", HttpStatus.BAD_REQUEST);
            }
            List<JobEntity> jobs = Jobrepository.findJobsByHrId(hr_id);

            if (jobs.isEmpty()) {
                throw new JobsApiException("No jobs found for HR ID: " + hr_id, HttpStatus.NOT_FOUND);
            }

            return jobs;
        } catch (JobsApiException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("Unexpected exception while fetching jobs for HR ID " + hr_id + ": " + e.getMessage());
            throw new JobsApiException("An error occurred while fetching jobs for HR ID: " + hr_id, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<ApplicationEntity> getApplicationsByEmpId(String empId) {
        try {
            if (empId == null || empId.isEmpty()) {
                throw new JobsApiException("Employee ID is required", HttpStatus.BAD_REQUEST);
            }
            List<ApplicationEntity> applications = applicationRepository.findByEmpId(empId);

            if (applications.isEmpty()) {
                throw new JobsApiException("No applications found for Employee ID: " + empId, HttpStatus.NOT_FOUND);
            }

            return applications;
        } catch (JobsApiException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("Unexpected exception while fetching applications for Employee ID " + empId + ": " + e.getMessage());
            throw new JobsApiException("An error occurred while fetching applications for Employee ID: " + empId, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean applyToJob(String empId, String jobId) {
        try {
            if (empId == null || empId.isEmpty()) {
                throw new JobsApiException("Employee ID is required", HttpStatus.BAD_REQUEST);
            }
            if (jobId == null || jobId.isEmpty()) {
                throw new JobsApiException("Job ID is required", HttpStatus.BAD_REQUEST);
            }

            ApplicationEntity isAlreadyApplied = applicationRepository.findByEmpIdAndJobId(empId, jobId);
            if(isAlreadyApplied != null){
                throw new JobsApiException("Employee already applied to this job", HttpStatus.BAD_REQUEST);
            }

            JobEntity job = Jobrepository.findById(jobId)
                    .orElseThrow(() -> new JobsApiException("Job not found for ID: " + jobId, HttpStatus.NOT_FOUND));

            System.out.println("Inside applyToJob service impl: job details - " + job);
            ApplicationEntity application = new ApplicationEntity();
            application.setEmpId(empId);
            application.setJobId(job.getJob_id());

            applicationRepository.save(application);

            return true;
        } catch (JobsApiException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("Unexpected error while applying to job with Job ID " + jobId + " and Employee ID " + empId + ": " + e.getMessage());
            throw new JobsApiException("An error occurred while applying to the job", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @Override
    public boolean deleteJobByJobId(String job_id){
        try {
            if (job_id == null || job_id.isEmpty()) {
                throw new JobsApiException("Job ID is required", HttpStatus.BAD_REQUEST);
            }
            JobEntity job = Jobrepository.findById(job_id)
                    .orElseThrow(() -> new JobsApiException("Job not found for ID: " + job_id, HttpStatus.NOT_FOUND));

            System.out.println("Inside deleteJobByJobId service impl: job details - " + job);

            applicationRepository.deleteByJobId(job_id);
            Jobrepository.deleteById(job_id);

            return true;
        } catch (JobsApiException e) {

            throw e;
        } catch (Exception e) {
            System.out.println("Unexpected error while deleting job with ID " + job_id + ": " + e.getMessage());
            throw new JobsApiException("An error occurred while deleting the job", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<ApplicationEntity> getApplicationsByJobId(String jobId) {
        try {
            if (jobId == null || jobId.isEmpty()) {
                throw new JobsApiException("Job ID is required", HttpStatus.BAD_REQUEST);
            }
            List<ApplicationEntity> applications = applicationRepository.findByJobId(jobId);
            if (applications.isEmpty()) {
                throw new JobsApiException("No applications found for job ID: " + jobId, HttpStatus.NOT_FOUND);
            }
            return applications;
        } catch (JobsApiException e) {
            throw e;
        } catch (Exception e) {
            // Log unexpected exceptions with context
            System.out.println("Unexpected error while retrieving applications for job ID " + jobId + ": " + e.getMessage());
            throw new JobsApiException("An error occurred while retrieving applications for job ID: " + jobId, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
