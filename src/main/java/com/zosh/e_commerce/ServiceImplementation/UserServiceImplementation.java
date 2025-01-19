package com.zosh.e_commerce.ServiceImplementation;

import com.zosh.e_commerce.Configuration.JwtProvider;
import com.zosh.e_commerce.Exception.UserNotFoundException;
import com.zosh.e_commerce.Model.User;
import com.zosh.e_commerce.Repository.UserRepository;
import com.zosh.e_commerce.ServiceInterface.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;

    public UserServiceImplementation(UserRepository userRepository, JwtProvider jwtProvider) {

        this.userRepository = userRepository;

        this.jwtProvider = jwtProvider;

    }
    @Override
    public User findUserById(Long userId) throws UserNotFoundException {

        User user = userRepository.findById(userId).get();

        if(user == null) throw new UserNotFoundException("User not found with id " + userId);

        return user;
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws UserNotFoundException {

        String email = jwtProvider.getEmailFromToken(jwt);

        User user = userRepository.findByEmail(email);

        if(user == null) throw new UserNotFoundException("User not found with email: " + email);

        return user;
    }
}
