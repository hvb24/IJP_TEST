package com.IJP.auth_service.service.impl;

import com.IJP.auth_service.entity.AuthHr;
import com.IJP.auth_service.entity.Role;
import com.IJP.auth_service.entity.User;
import com.IJP.auth_service.feign.HrInterface;
import com.IJP.auth_service.payload.LoginDTO;
import com.IJP.auth_service.payload.RegisterDTO;
import com.IJP.auth_service.repository.RoleRepository;
import com.IJP.auth_service.repository.UserRepository;
import com.IJP.auth_service.security.JwtTokenProvider;
import com.IJP.auth_service.service.AuthService;
import com.netflix.discovery.provider.Serializer;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    HrInterface hrInterface;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public String Mylogin(LoginDTO loginDTO) {
        AuthHr authHr = hrInterface.getHrByEmail(loginDTO.getUsernameOrEmail()).getBody();
        if(authHr == null) {
            return "";
        }
        if(authHr.getPassword().equals(loginDTO.getPassword())) {
            return "login successful";
        }
    return "";
    }

    @Override
    public String login(LoginDTO loginDto) {
        System.out.println("inside the login service"+loginDto);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsernameOrEmail(), loginDto.getPassword()
                ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
//        System.out.println("inside the login controller after the checks"+loginDto);
        System.out.println("Authentication Object: " + authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        return token;
    }

    @Override
    public String register(RegisterDTO registerDto) {

        if(userRepository.existsByUsername(registerDto.getUsername())){
            throw new RuntimeException("username is already exists");
        }

        // add check for email exists in database
        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw new RuntimeException("email is already exists");
        }

        // assign a role to the user
        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        System.out.println("inside the register service after creating the user"+user);

        Optional<Role> roleOptional = roleRepository.findByName(registerDto.getRoleName());
        if (!roleOptional.isPresent()) {
            throw new RuntimeException("Role not found: " + registerDto.getRoleName());
        }

        // Assign the role to the user
        Set<Role> roles = new HashSet<>();
        roles.add(roleOptional.get());
        user.setRoles(roles);

        // Save the user in the database
        userRepository.save(user);

        return "User registered successfully";
    }

    @Override
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }
}
