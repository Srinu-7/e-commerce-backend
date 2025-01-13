package com.zosh.e_commerce.Service;

import com.zosh.e_commerce.Exception.UserException;
import com.zosh.e_commerce.Model.User;
public interface UserService {

    public User findUserById(Long userId) throws UserException;

    public User findUserProfileByJwt(String jwt) throws UserException;
}
