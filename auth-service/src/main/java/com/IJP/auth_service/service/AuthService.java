package com.IJP.auth_service.service;

import com.IJP.auth_service.entity.Role;
import com.IJP.auth_service.payload.LoginDTO;
import com.IJP.auth_service.payload.RegisterDTO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface AuthService {
    String Mylogin(LoginDTO loginDTO);
    String login(LoginDTO loginDto);
    String register(RegisterDTO registerDto);
    Role createRole(Role role);
    List<Role> getAllRole();
}
