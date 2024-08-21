package com.IJP.hr.controller;

import com.IJP.hr.entity.HrEntity;
import com.IJP.hr.exception.HrApiException;
import com.IJP.hr.payload.ApiResponse;
import com.IJP.hr.payload.ErrorDetails;
import com.IJP.hr.services.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/hr")
public class HrController {

    @Autowired
    private HrService hrService;

    @GetMapping("/{email}")
    public ResponseEntity<ApiResponse<HrEntity>> getHrByEmail(@PathVariable String email) {
        try {
            HrEntity res = hrService.getHrByEmail(email);
            if (res == null) {
                ApiResponse<HrEntity> response = new ApiResponse<>(null, "HR not found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            ApiResponse<HrEntity> response = new ApiResponse<>(res, null, HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (HrApiException e) {
            ApiResponse<HrEntity> response = new ApiResponse<>(null, e.getMessage(), e.getStatus());
            return new ResponseEntity<>(response, e.getStatus());
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HrEntity>> postHr(@RequestBody HrEntity hrEntity) {
        try {
            HrEntity res = hrService.postHr(hrEntity);
            ApiResponse<HrEntity> response = new ApiResponse<>(res, null, HttpStatus.CREATED);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (HrApiException e) {
            ApiResponse<HrEntity> response = new ApiResponse<>(null, e.getMessage(), e.getStatus());
            return new ResponseEntity<>(response, e.getStatus());
        }
    }

}
