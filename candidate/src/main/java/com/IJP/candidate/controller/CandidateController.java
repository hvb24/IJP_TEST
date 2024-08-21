package com.IJP.candidate.controller;

import com.IJP.candidate.entity.Candidate;
import com.IJP.candidate.exception.CandidateApiException;
import com.IJP.candidate.services.CandidateServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.IJP.candidate.payload.ApiResponse;

@RestController
@RequestMapping("/candidate")
public class CandidateController {

    @Autowired
    private CandidateServices candidateService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Candidate>> getCandidate(@PathVariable String id){
        try {
            Candidate data = candidateService.getCandidateId(id);
            if (data == null) {
                ApiResponse<Candidate> response = new ApiResponse<>(null, "Candidate not found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            ApiResponse<Candidate> response = new ApiResponse<>(data, "Candidate retrieved successfully", HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CandidateApiException e) {
            ApiResponse<Candidate> response = new ApiResponse<>(null, e.getMessage(), e.getStatus());
            return new ResponseEntity<>(response, e.getStatus());
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Candidate>> postCandidate(@RequestBody Candidate candidate) {
        try {
            Candidate res = candidateService.postCandidate(candidate);
            ApiResponse<Candidate> response = new ApiResponse<>(res, "Candidate created successfully", HttpStatus.CREATED);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (CandidateApiException e) {
            ApiResponse<Candidate> response = new ApiResponse<>(null, e.getMessage(),e.getStatus());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
