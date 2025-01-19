package com.zosh.e_commerce.Controller;

import com.zosh.e_commerce.Exception.UserNotFoundException;
import com.zosh.e_commerce.Model.User;
import com.zosh.e_commerce.ServiceInterface.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;

    }

    @GetMapping("/profile")
    public ResponseEntity<User> userProfile(@RequestHeader("Authorization") String jwt) throws UserNotFoundException {

        User user = userService.findUserProfileByJwt(jwt);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
