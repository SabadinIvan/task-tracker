package com.sabadin.taskspringapp.security.service;

import com.sabadin.taskspringapp.security.jwt.JwtTokenUtil;
import com.sabadin.taskspringapp.security.model.dto.LoginRequestDto;
import com.sabadin.taskspringapp.security.model.dto.SignupRequestDto;
import com.sabadin.taskspringapp.security.model.dto.UserAuthResponse;
import com.sabadin.taskspringapp.security.model.entity.User;
import com.sabadin.taskspringapp.security.model.entity.UserLogon;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public void userSignup(SignupRequestDto dto) {
        log.info("called AuthServiceImpl -> userSignup");
        UserLogon userLogon = createUser(dto);
        log.info("userLogon -> " + userLogon);


//        User user = getUserBySignupRequestDtoAndUserLogon(dto);
//        User savedUser = userService.save(user);
//        UserLogon userLogon = getUserLogonBySignupRequestDto(dto, savedUser);
//        UserLogon savedUserLogon = userDetailsService.save(userLogon);
//        authenticate(dto.getLogonName(), dto.getPassword());
//        return getUserAuthResponse(userLogon);
    }

    @Override
    public UserAuthResponse userLogin(LoginRequestDto dto) {
        log.info("called AuthServiceImpl -> userLogin");
        authenticate(dto.getLogonName(), dto.getPassword());
        UserLogon userLogon = (UserLogon) userDetailsService.loadUserByUsername(dto.getLogonName());
        return getUserAuthResponse(userLogon);
    }

    private UserLogon createUser(SignupRequestDto dto) {
        User user = new User();
        user.setVersion(1);
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setMiddleName(dto.getMiddleName());
        user.setEmail(dto.getEmail());
        User savedUser = userService.save(user);
        UserLogon userLogon = new UserLogon();
        userLogon.setVersion(1);
        userLogon.setLogonName(dto.getLogonName());
        userLogon.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        userLogon.setUser(savedUser);
        return userDetailsService.save(userLogon);
    }

    private UserLogon getUserLogonBySignupRequestDto(SignupRequestDto dto, User user) {
        UserLogon userLogon = new UserLogon();
        userLogon.setVersion(1);
        userLogon.setLogonName(dto.getLogonName());
        userLogon.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        userLogon.setUser(user);
        return  userLogon;
    }

    private User getUserBySignupRequestDtoAndUserLogon(SignupRequestDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setVersion(1);
        return user;
    }

    private void authenticate(String logonName, String password) {
        log.info("calling AuthService -> authenticate");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(logonName, password)
        );
    }

    private UserAuthResponse getUserAuthResponse(UserLogon userLogon) {
        var jwtToken = jwtTokenUtil.generateJwtToken(userLogon);
        var refreshToken = jwtTokenUtil.generateRefreshJwtToken(userLogon);
        return UserAuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .logonName(userLogon.getLogonName())
                .build();
    }
}
