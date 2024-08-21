package com.IJP.jobs.controller;

import com.IJP.jobs.entity.ApplicationEntity;
import com.IJP.jobs.entity.JobEntity;
import com.IJP.jobs.exceptions.JobsApiException;
import com.IJP.jobs.payload.ApiResponse;
import com.IJP.jobs.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Jobs")
@CrossOrigin(origins = "http://localhost:4000")
public class JobController {

    @Autowired
    private JobService Jobservice;

    @PostMapping("/create/{hrId}")
    public ResponseEntity<ApiResponse<JobEntity>> postJobByHrId(@RequestBody JobEntity jobEntity, @PathVariable String hrId){
        try {
            System.out.println("inside post JobById");
            JobEntity job = Jobservice.postJob(jobEntity, hrId);
            ApiResponse<JobEntity> response = new ApiResponse<>(job, "Job Created successfully", HttpStatus.CREATED);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (JobsApiException e) {
            ApiResponse<JobEntity> response = new ApiResponse<>(null, e.getMessage(), e.getStatus());
            return new ResponseEntity<>(response, e.getStatus());
        } catch (Exception e) {
            ApiResponse<JobEntity> response = new ApiResponse<>(null, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<JobEntity>>> getAllJobs(){
        try {
            List<JobEntity> jobs = Jobservice.getAllJobs();
            ApiResponse<List<JobEntity>> response = new ApiResponse<>(jobs, "All Jobs fetched successfully", HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (JobsApiException e) {
            ApiResponse<List<JobEntity>> response = new ApiResponse<>(null, e.getMessage(), e.getStatus());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ApiResponse<List<JobEntity>> response = new ApiResponse<>(null, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/{job_id}")
    public ResponseEntity<ApiResponse<JobEntity>> getJobById(@PathVariable String job_id){
        try {
            System.out.println("inside getJobById: " + job_id);
            JobEntity job = Jobservice.getJobById(job_id);
            if (job != null) {
                ApiResponse<JobEntity> response = new ApiResponse<>(job, "Job fetched successfully", HttpStatus.OK);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                ApiResponse<JobEntity> response = new ApiResponse<>(null, "Job not found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (JobsApiException e) {
            ApiResponse<JobEntity> response = new ApiResponse<>(null, e.getMessage(), e.getStatus());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }  catch (Exception e) {
            ApiResponse<JobEntity> response = new ApiResponse<>(null, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/jobsByHrId/{hr_id}")
    public ResponseEntity<ApiResponse<List<JobEntity>>> getJobByHrId(@PathVariable String hr_id){
        try {
            System.out.println("inside getJobByHrId: " + hr_id);
            List<JobEntity> jobs = Jobservice.getJobByHrId(hr_id);
            System.out.println(jobs);

            if (jobs != null && !jobs.isEmpty()) {
                ApiResponse<List<JobEntity>> response = new ApiResponse<>(jobs, "Jobs fetched successfully", HttpStatus.OK);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                ApiResponse<List<JobEntity>> response = new ApiResponse<>(null, "No jobs found for the given HR ID", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        }catch (JobsApiException e) {
            ApiResponse<List<JobEntity>> response = new ApiResponse<>(null, e.getMessage(), e.getStatus());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ApiResponse<List<JobEntity>> response = new ApiResponse<>(null, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/by-empid/{empId}")
    public ResponseEntity<ApiResponse<List<ApplicationEntity>>> getApplicationsByEmpId(@PathVariable String empId){
        try {
            List<ApplicationEntity> applications = Jobservice.getApplicationsByEmpId(empId);

            if (applications.isEmpty()) {
                ApiResponse<List<ApplicationEntity>> response = new ApiResponse<>(null, "No applications found for the given employee ID", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                ApiResponse<List<ApplicationEntity>> response = new ApiResponse<>(applications, "Applications fetched successfully", HttpStatus.OK);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }catch (JobsApiException e) {
            ApiResponse<List<ApplicationEntity>> response = new ApiResponse<>(null, e.getMessage(), e.getStatus());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }  catch (Exception e) {
            ApiResponse<List<ApplicationEntity>> response = new ApiResponse<>(null, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/post-job-empID")
    public ResponseEntity<ApiResponse<String>> applyToJob(@RequestParam("empId") String empId, @RequestParam("jobId") String jobId){
        System.out.println("inside applyToJob controller: empId=" + empId + ", jobId=" + jobId);

        try {
            boolean isApplied = Jobservice.applyToJob(empId, jobId);
            if (isApplied) {
                ApiResponse<String> response = new ApiResponse<>("Application submitted successfully.", null, HttpStatus.OK);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                ApiResponse<String> response = new ApiResponse<>(null, "Job application failed", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }catch (JobsApiException e) {
            ApiResponse<String> response = new ApiResponse<>(null, e.getMessage(), e.getStatus());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>(null, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<ApiResponse<String>> deleteJobByJobId (@PathVariable String jobId ){
        try {
            boolean isDeleted = Jobservice.deleteJobByJobId(jobId);
            if (isDeleted) {
                ApiResponse<String> response = new ApiResponse<>("Job deleted successfully.", null, HttpStatus.OK);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                ApiResponse<String> response = new ApiResponse<>(null, "Job deletion failed", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }catch (JobsApiException e) {
            ApiResponse<String> response = new ApiResponse<>(null, e.getMessage(), e.getStatus());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>(null, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-emp-jobId/{jobId}")
    public ResponseEntity<ApiResponse<List<ApplicationEntity>>> getApplicationsByJobId(@PathVariable String jobId){
        try {
            System.out.println("inside getApplicationsByJobId: " + jobId);
            List<ApplicationEntity> applications = Jobservice.getApplicationsByJobId(jobId);
            if (applications.isEmpty()) {
                ApiResponse<List<ApplicationEntity>> response = new ApiResponse<>(null, "No applications found for the given job ID", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            ApiResponse<List<ApplicationEntity>> response = new ApiResponse<>(applications, "Applications fetched successfully", HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (JobsApiException e) {
            ApiResponse<List<ApplicationEntity>> response = new ApiResponse<>(null, e.getMessage(), e.getStatus());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            ApiResponse<List<ApplicationEntity>> response = new ApiResponse<>(null, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
