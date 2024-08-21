package com.IJP.hr.services.impl;

import com.IJP.hr.entity.HrEntity;
import com.IJP.hr.exception.HrApiException;
import com.IJP.hr.exception.ResourceNotFoundException;
import com.IJP.hr.repository.HrRepository;
import com.IJP.hr.services.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class HrServiceImpl implements HrService {

    @Autowired
    private HrRepository hrRepository;

    @Override
    public HrEntity getHrByEmail(String email) {
        try {
            if(email==null || email.isEmpty()) {
                throw new HrApiException("Email is required", HttpStatus.BAD_REQUEST);
            }
            return hrRepository.findByEmail(email)
                    .orElseThrow(() -> new HrApiException("No such email found", HttpStatus.NOT_FOUND));
        }catch (HrApiException e) {
            throw e;
        }catch (Exception e) {
            System.out.println("Unexpected exception: " + e.getMessage());
            throw new HrApiException("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public HrEntity postHr(HrEntity hrEntity) {
        try {
            if (hrEntity == null) {
                throw new HrApiException("HR info is required", HttpStatus.BAD_REQUEST);
            }
            String email = hrEntity.getEmail();
            if (hrRepository.findByEmail(email).isPresent()) {
                throw new HrApiException("Email already exists", HttpStatus.BAD_REQUEST);
            }
            HrEntity res = hrRepository.save(hrEntity);
            if (res == null) {
                throw new HrApiException("HR not created", HttpStatus.BAD_REQUEST);
            }
            return res;
        } catch (HrApiException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            throw new HrApiException("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
