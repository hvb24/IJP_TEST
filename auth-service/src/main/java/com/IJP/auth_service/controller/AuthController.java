package com.IJP.auth_service.controller;

import com.IJP.auth_service.entity.Role;
import com.IJP.auth_service.payload.LoginDTO;
import com.IJP.auth_service.payload.RegisterDTO;
import com.IJP.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4000")
public class AuthController {

    @Autowired
    private  AuthService authService;

    @GetMapping("/hr")
    public ResponseEntity<String> Mylogin(@RequestBody LoginDTO loginDto) {
        System.out.println(loginDto);
        String response = authService.login(loginDto);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDto){
        String response = authService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDto){
        System.out.println("inside the auth login"+loginDto);
        String response = authService.login(loginDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@RequestBody Role role){
        Role response = authService.createRole(role);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRole(){
        List<Role> response = authService.getAllRole();
        return ResponseEntity.ok(response);
    }

}
