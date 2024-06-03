package com.devexperts.chatapp.model.dto;

import com.devexperts.chatapp.model.enums.StatusEnum;

import javax.validation.constraints.*;
import java.util.Date;

public class UserViewAndEditDto {

    private Long id;

    @NotEmpty(message = "Enter a username.")
    @Size(min = 3, max = 10, message = "Username length must be between 3 and 10 characters!")
    private String username;

    @NotEmpty(message = "Enter an email.")
    @Email(message = "Not a valid email address.")
    private String email;

    @NotNull(message = "Enter the age")
    @Min(value = 14, message = "Age must be at least 14")
    @Max(value = 100, message = "Age must be at most 100")
    private Integer age;

    @NotEmpty(message = "Enter a country.")
    private String country;

    private Date date;

    private StatusEnum status;

    public UserViewAndEditDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public UserViewAndEditDto setStatus(StatusEnum status) {
        this.status = status;
        return this;
    }
}
