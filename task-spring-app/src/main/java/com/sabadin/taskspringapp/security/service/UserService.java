package com.sabadin.taskspringapp.security.service;

import com.sabadin.taskspringapp.security.model.entity.User;
import com.sabadin.taskspringapp.security.model.entity.UserLogon;

public interface UserService {
    boolean isExistEmail(String email);
    User save(User user);
    User getUserByUserLogon(UserLogon userLogon);
}
