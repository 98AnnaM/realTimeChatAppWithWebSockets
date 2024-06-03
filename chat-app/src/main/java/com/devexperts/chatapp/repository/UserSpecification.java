package com.devexperts.chatapp.repository;

import com.devexperts.chatapp.model.dto.UserSearchDto;
import com.devexperts.chatapp.model.entity.UserEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification implements Specification<UserEntity> {

    private final UserSearchDto searchUserDto;

    public UserSpecification(UserSearchDto searchUserDto) {
        this.searchUserDto = searchUserDto;
    }

    @Override
    public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        final List<Predicate> predicates = new ArrayList<>();

        if (searchUserDto.getUsername() != null && !searchUserDto.getUsername().isEmpty()) {
            predicates.add(cb.like(root.get("username"), "%" + searchUserDto.getUsername() + "%"));
        }

        if (searchUserDto.getUsername() != null && !searchUserDto.getUsername().isEmpty()) {
            predicates.add(cb.like(root.get("username"), "%" + searchUserDto.getUsername() + "%"));
        }

        if (searchUserDto.getEmail() != null && !searchUserDto.getEmail().isEmpty()) {
            predicates.add(cb.like(root.get("email"), "%" + searchUserDto.getEmail() + "%"));
        }

        if (searchUserDto.getMinAge() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("age"), searchUserDto.getMinAge()));
        }

        if (searchUserDto.getMaxAge() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("age"), searchUserDto.getMaxAge()));
        }

        if (searchUserDto.getCountry() != null && !searchUserDto.getCountry().isEmpty()) {
            predicates.add(cb.like(root.get("country"), "%" + searchUserDto.getCountry() + "%"));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }


}
