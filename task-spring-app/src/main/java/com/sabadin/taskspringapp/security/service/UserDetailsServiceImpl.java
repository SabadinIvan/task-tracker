package com.sabadin.taskspringapp.security.service;

import com.sabadin.taskspringapp.security.model.entity.UserLogon;
import com.sabadin.taskspringapp.security.repository.UserLogonRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserLogonRepository userLogonRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserLogon userLogon = userLogonRepository.findByLogonName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with user name " + username + " not found"));
        return userLogon;
    }

    public boolean iExistsByLogonName(String logonName) {
        return userLogonRepository.existsByLogonName(logonName);
    }

    public UserLogon save(UserLogon userLogon) {
        return userLogonRepository.save(userLogon);
    }
}
