package com.sheigert.medical_records_api.services;

import com.sheigert.medical_records_api.dto.request.LoginRequest;
import com.sheigert.medical_records_api.dto.request.RegisterRequest;
import com.sheigert.medical_records_api.dto.response.AuthResponse;
import com.sheigert.medical_records_api.dto.response.UserResponse;
import com.sheigert.medical_records_api.entity.User;
import com.sheigert.medical_records_api.enums.Role;
import com.sheigert.medical_records_api.repository.UserRepository;
import com.sheigert.medical_records_api.security.CustomUserDetails;
import com.sheigert.medical_records_api.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered; " + request.getEmail());
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user);

        String token = jwtService.generateToken(new CustomUserDetails(user));

        return new AuthResponse(user.getId(), token, user.getEmail(), user.getFullName(), user.getRole());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String token = jwtService.generateToken(new CustomUserDetails(user));

        return new AuthResponse(user.getId(), token, user.getEmail(), user.getFullName(), user.getRole());
    }

    public List<UserResponse> findByRole(Role role) {
        return userRepository.findByRole(role).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<UserResponse> findAvailableForPatient() {
        return userRepository.findPatientRoleUsersWithoutPatientRecord().stream()
                .map(this::toResponse)
                .toList();
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getRole());
    }
}
