package com.sheigert.medical_records_api.repository;

import com.sheigert.medical_records_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
