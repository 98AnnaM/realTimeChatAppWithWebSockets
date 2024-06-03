package com.devexperts.chatapp.model.dto;

import com.devexperts.chatapp.validation.FieldMatch;
import com.devexperts.chatapp.validation.UniqueEmail;
import com.devexperts.chatapp.validation.UniqueUsername;

import javax.validation.constraints.*;
import java.util.Date;

@FieldMatch(first = "password", second = "confirmPassword", message = "Passwords do not match.")
public class UserRegisterDto {

    @NotEmpty(message = "Enter a username.")
    @UniqueUsername(message = "This username is already in use.")
    @Size(min = 3, max = 20, message = "Username length must be between 3 and 10 characters!")
    private String username;

    @NotEmpty(message = "Enter an email.")
    @Email(message = "Not a valid email address.")
    @UniqueEmail(message = "This email is already in use.")
    private String email;

    @NotNull(message = "Enter the age")
    @Min(value = 14, message = "Age must be at least 14")
    @Max(value = 100, message = "Age must be at most 100")
    private Integer age;

    @NotEmpty(message = "Enter a country.")
    private String country;

    @NotEmpty(message = "Password cannot be empty.")
    @Size(min = 3, max = 20, message = "Password length must be between 3 and 20 characters!")
    private String password;

    private String confirmPassword;

    private Date date;

    public UserRegisterDto() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
