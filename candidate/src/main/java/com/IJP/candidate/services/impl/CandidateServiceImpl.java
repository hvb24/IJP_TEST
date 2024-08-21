package com.IJP.candidate.services.impl;

import com.IJP.candidate.exception.CandidateApiException;
import com.IJP.candidate.exception.ResourceNotFoundException;
import com.IJP.candidate.repository.CandidateRepository;
import com.IJP.candidate.services.CandidateServices;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.IJP.candidate.entity.Candidate;

import java.util.Optional;

@Service
public class CandidateServiceImpl implements CandidateServices {

    @Autowired
    private CandidateRepository candidateRepo;

    @Override
    public Candidate getCandidateId(String id) {
        try {
            if (id == null || id.isEmpty()) {
                throw new CandidateApiException("Candidate ID is required", HttpStatus.BAD_REQUEST);
            }
            return candidateRepo.findByEmail(id)
                    .orElseThrow(() -> new CandidateApiException("Candidate not found with ID: " + id, HttpStatus.NOT_FOUND));
        } catch (CandidateApiException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e.getMessage());
            throw new CandidateApiException("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String getCandidateEmail(String email){
        try {
            if (email == null || email.isEmpty()) {
                throw new CandidateApiException("Email is required", HttpStatus.BAD_REQUEST);
            }

            Optional<Candidate> candidate = candidateRepo.findByEmail(email);
            if (candidate.isPresent()) {
                return candidate.get().getEmpID();  // Assuming Candidate has a method getEmpID()
            } else {
                throw new CandidateApiException("Candidate not found with email: " + email, HttpStatus.NOT_FOUND);
            }
        } catch (CandidateApiException e) {
            // Rethrow the specific exception to be handled by the controller
            throw e;
        } catch (Exception e) {
            // Handle any other exceptions
            System.out.println("Unexpected exception: " + e.getMessage());
            throw new CandidateApiException("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Candidate postCandidate(Candidate candidate){
//        return candidateRepo.save(candidate);
        try {
            if (candidate == null) {
                throw new CandidateApiException("Candidate information is required", HttpStatus.BAD_REQUEST);
            }
            String email = candidate.getEmail();
            if (email == null || email.isEmpty()) {
                throw new CandidateApiException("Candidate email is required", HttpStatus.BAD_REQUEST);
            }
            Optional<Candidate> existingCandidate = candidateRepo.findByEmail(email);
            if (existingCandidate.isPresent()) {
                throw new CandidateApiException("Candidate with this email already exists", HttpStatus.BAD_REQUEST);
            }
            return candidateRepo.save(candidate);

        } catch (CandidateApiException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e.getMessage());
            throw new CandidateApiException("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
