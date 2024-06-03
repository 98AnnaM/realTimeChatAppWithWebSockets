package com.devexperts.chatapp.model.dto;

public class UserSearchDto {

    private String username;

    private String email;

    private Integer minAge;

    private Integer maxAge;

    private String country;

    public UserSearchDto() {
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

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (username != null && !username.isEmpty()) {
            sb.append(String.format("Name: " + username + " "));
        }

        if (email != null && !email.isEmpty()) {
            sb.append(String.format("Email: " + email + " "));
        }

        if (minAge != null) {
            sb.append(String.format("Min age: " + minAge + "min "));
        }

        if (maxAge != null) {
            sb.append(String.format("Max age: " + maxAge + "min "));
        }

        if (country != null && !country.isEmpty()) {
            sb.append(String.format("Country: " + country + " "));
        }
        return sb.toString();
    }

    public boolean isEmpty() {
        return (username == null || username.isEmpty()) &&
                (email == null || email.isEmpty()) &&
                maxAge == null &&
                minAge == null &&
                (country == null || country.isEmpty());
    }
}
