package com.sheigert.medical_records_api.dto.response;

import com.sheigert.medical_records_api.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private Long id;
    private String token;
    private String email;
    private String fullName;
    private Role role;
}
