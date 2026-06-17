package com.sabadin.taskspringapp.security.service;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Если нет аутентификации или аноним — возвращаем system
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication.getPrincipal() instanceof String &&
                        authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.of("system");
        }
        return Optional.of(authentication.getName());
    }
}
