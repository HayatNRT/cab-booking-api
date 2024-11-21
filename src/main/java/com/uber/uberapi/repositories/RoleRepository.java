package com.uber.uberapi.repositories;

import com.uber.uberapi.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByName(String name);
}
