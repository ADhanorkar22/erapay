package com.edsom.EraPay.Security;


import com.edsom.EraPay.Entities.User;
import com.edsom.EraPay.Repos.RoleRepo;
import com.edsom.EraPay.Repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {



    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User employee = userRepo.findByUserId(username);
        if(employee==null)
        {
               throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                employee.getUserId(),
                employee.getPassword(),
                List.of(new SimpleGrantedAuthority(employee.getRole().getUserType().toString())) // Use role for authorities
        );
    }
}

