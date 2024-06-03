package com.devexperts.chatapp.util;

import com.devexperts.chatapp.user_details.CustomUserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestUserDataService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!username.equals("admin")) {
            return new CustomUserDetails(2L,
                    "user",
                    "12345",
                    List.of(new SimpleGrantedAuthority("ROLE_USER")));
        }

        return new CustomUserDetails(3L,
                "admin",
                "12345",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }
}
