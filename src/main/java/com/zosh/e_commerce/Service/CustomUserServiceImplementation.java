package com.zosh.e_commerce.Service;

import com.zosh.e_commerce.Model.User;
import com.zosh.e_commerce.Repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserServiceImplementation implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);

        if(user == null) throw new UsernameNotFoundException("User not found with email:"+user.getEmail());

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        org.springframework.security.core.userdetails.User

        return null;
    }
}
