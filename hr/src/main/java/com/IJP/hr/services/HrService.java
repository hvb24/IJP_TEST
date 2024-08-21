package com.IJP.hr.services;

import com.IJP.hr.entity.HrEntity;

public interface HrService {
    HrEntity getHrByEmail(String email);
    HrEntity postHr(HrEntity hrEntity);
}
