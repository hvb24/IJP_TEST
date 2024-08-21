package com.IJP.auth_service.feign;

import com.IJP.auth_service.entity.AuthHr;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="HR", url = "http://localhost:8081")
public interface HrInterface {

    @GetMapping("/hr/{email}")
    public ResponseEntity<AuthHr> getHrByEmail(@PathVariable String email);


}
