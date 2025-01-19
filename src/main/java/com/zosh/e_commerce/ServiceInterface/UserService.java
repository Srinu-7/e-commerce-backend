package com.zosh.e_commerce.ServiceInterface;

import com.zosh.e_commerce.Exception.UserNotFoundException;
import com.zosh.e_commerce.Model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public User findUserById(Long userId) throws UserNotFoundException;

    public User findUserProfileByJwt(String jwt) throws UserNotFoundException;
}
