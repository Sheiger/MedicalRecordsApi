package com.sheigert.medical_records_api.dto.response;

import com.sheigert.medical_records_api.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String fullName;
    private String email;
    private Role role;
}
