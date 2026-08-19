package com.sheigert.medical_records_api.repository;

import com.sheigert.medical_records_api.entity.User;
import com.sheigert.medical_records_api.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRole(Role role);

    @Query("SELECT u FROM User u WHERE u.role = com.sheigert.medical_records_api.enums.Role.PATIENT " +
            "AND u.id NOT IN (SELECT p.user.id FROM Patient p)")
    List<User> findPatientRoleUsersWithoutPatientRecord();
}
