package com.IJP.candidate.services;

import com.IJP.candidate.entity.Candidate;
import org.springframework.stereotype.Service;

@Service
public interface CandidateServices{

    Candidate getCandidateId(String id);
    String getCandidateEmail(String email);
    Candidate postCandidate(Candidate candidate);

}
