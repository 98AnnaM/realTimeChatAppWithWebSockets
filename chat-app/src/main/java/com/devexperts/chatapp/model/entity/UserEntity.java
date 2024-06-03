package com.devexperts.chatapp.model.entity;

import com.devexperts.chatapp.model.enums.StatusEnum;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @javax.persistence.ManyToMany(fetch = javax.persistence.FetchType.EAGER)
    private List<RoleEntity> roles;

    public UserEntity() {
    }

    public String getUsername() {
        return username;
    }

    public UserEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public UserEntity setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public UserEntity setCountry(String country) {
        this.country = country;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public UserEntity setDate(Date date) {
        this.date = date;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public UserEntity setStatus(StatusEnum status) {
        this.status = status;
        return this;
    }

    public List<RoleEntity> getRoles() {
        return roles;
    }

    public UserEntity setRoles(List<RoleEntity> roles) {
        this.roles = roles;
        return this;
    }
}
