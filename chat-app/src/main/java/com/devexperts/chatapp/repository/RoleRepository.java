package com.devexperts.chatapp.repository;

import com.devexperts.chatapp.model.entity.RoleEntity;
import com.devexperts.chatapp.model.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByRole(RoleEnum role);
}
