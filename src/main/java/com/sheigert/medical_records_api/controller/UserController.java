package com.sheigert.medical_records_api.controller;
import com.sheigert.medical_records_api.dto.response.UserResponse;
import com.sheigert.medical_records_api.enums.Role;
import com.sheigert.medical_records_api.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<List<UserResponse>> findByRole(@RequestParam Role role) {
        return ResponseEntity.ok(userService.findByRole(role));
    }

    @GetMapping("/available-for-patient")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<List<UserResponse>> findAvailableForPatient() {
        return ResponseEntity.ok(userService.findAvailableForPatient());
    }
}