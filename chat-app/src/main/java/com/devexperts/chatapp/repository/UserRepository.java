package com.devexperts.chatapp.repository;

import com.devexperts.chatapp.model.entity.UserEntity;
import com.devexperts.chatapp.model.enums.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>,
        JpaSpecificationExecutor<UserEntity> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<UserEntity> findAllByAgeBeforeOrderByAgeAsc(Integer age);

    List<UserEntity> findAllByCountryIsOrderByUsername(String country);

    Optional<UserEntity> findByUsername(String username);

    List<UserEntity> findAllByStatus(StatusEnum status);
}
