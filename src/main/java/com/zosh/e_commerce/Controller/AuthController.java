package com.zosh.e_commerce.Controller;

import com.zosh.e_commerce.Configuration.JwtProvider;
import com.zosh.e_commerce.Exception.UserNotFoundException;
import com.zosh.e_commerce.Model.Cart;
import com.zosh.e_commerce.Model.User;
import com.zosh.e_commerce.Repository.UserRepository;
import com.zosh.e_commerce.Request.LoginRequest;
import com.zosh.e_commerce.Response.AuthResponse;
import com.zosh.e_commerce.ServiceImplementation.AuthenticationServiceImplementation;
import com.zosh.e_commerce.ServiceInterface.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationServiceImplementation authenticationServiceImplementation;

    private final CartService cartService;

    public AuthController(UserRepository userRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder, AuthenticationServiceImplementation authenticationServiceImplementation, CartService cartService) {

        this.userRepository = userRepository;

        this.jwtProvider = jwtProvider;

        this.passwordEncoder = passwordEncoder;

        this.authenticationServiceImplementation = authenticationServiceImplementation;

        this.cartService = cartService;
    }


    @PostMapping("/signUp")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserNotFoundException {

           String email = user.getEmail();

           String password = user.getPassword();

           String firstName = user.getFirstName();

           String lastName = user.getLastName();

           User isEmailExists = userRepository.findByEmail(email);

           if(isEmailExists != null) throw new UserNotFoundException("email is already used with another account"+email);

           User createdUser = new User();

           createdUser.setEmail(email);

           createdUser.setPassword(passwordEncoder.encode(password));

           createdUser.setFirstName(firstName);

           createdUser.setLastName(lastName);

           createdUser.setPhone(user.getPhone());

           createdUser.setRole(user.getRole());

           createdUser.setCreatedAt(LocalDateTime.now());

           User savedUser = userRepository.save(createdUser);

           Cart cart = cartService.createCart(savedUser);

           Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);

           SecurityContextHolder.getContext().setAuthentication(authentication);

           String token = jwtProvider.generateToken(authentication);

           AuthResponse authResponse = new AuthResponse(token,"Sign up successful");

           return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signIn")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest) throws UserNotFoundException {

        String userName = loginRequest.getEmail();

        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(userName,password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse(token,"Sign In successful");

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    private Authentication authenticate(String userName, String password) {

        UserDetails userDetails = authenticationServiceImplementation.loadUserByUsername(userName);

        if(userDetails == null) throw new BadCredentialsException("invalid username");

        if(!passwordEncoder.matches(password, userDetails.getPassword())) throw new BadCredentialsException("invalid password");

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
