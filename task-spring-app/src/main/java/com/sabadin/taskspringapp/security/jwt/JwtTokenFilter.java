package com.sabadin.taskspringapp.security.jwt;

import com.sabadin.taskspringapp.security.service.UserDetailsServiceImpl;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
@AllArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Called JwtTokenFilter -> doFilterInternal");
        String token = getTokenFromRequest(request);
        if (token == null || token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        // Логируем все входящие запросы
//        log.info("=== JwtAuthFilter ===");
//        log.info("Request URI: " + request.getRequestURI());
//        log.info("Request Method: " + request.getMethod());
//        log.info("Origin: " + request.getHeader("Origin"));
//        log.info("Authorization: " + request.getHeader("Authorization"));
//
//        // Выводим все заголовки
//        Collections.list(request.getHeaderNames()).forEach(headerName -> {
//            log.info(headerName + ": " + request.getHeader(headerName));
//        });
//
//        // Пропускаем OPTIONS запросы (preflight)
//        if (HttpMethod.OPTIONS.name().equals(request.getMethod())) {
//            log.info("Skipping JWT check for OPTIONS request");
//            filterChain.doFilter(request, response);
//            return;
//        }

//        // Пропускаем публичные endpoints
//        String requestURI = request.getRequestURI();
//        log.info("requestURI -> " + requestURI);
//        if (requestURI.startsWith("/api/auth/") ||
//                requestURI.startsWith("/api/public/") ||
//                requestURI.equals("/error")) {
//            log.info("Skipping JWT check for public endpoint: " + requestURI);
//            filterChain.doFilter(request, response);
//            return;
//        }

        String userName = jwtTokenUtil.extractUsername(token);
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            log.info("token -> " + token);
//            var isTokenValid = tokenRepository.findByToken(jwt).map(t -> !t.isExpired() && !t.isRevoked()).orElse(false);
//            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                        userDetails,
//                        null,
//                        userDetails.getAuthorities()
//                );
//                authToken.setDetails(
//                        new WebAuthenticationDetailsSource().buildDetails(request)
//                );
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
        }

//        try {
//            if (jwtTokenUtil.validateJwtToken(token)) {
//                String userName = jwtTokenUtil.extractUsername(token);
//                log.info("userName -> " + userName);
//                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//            }
//        } catch(JwtException ex) {
//            handlerExceptionResolver.resolveException(request, response, null, ex);
//        }
        log.info("Exiting doFilterInternal");
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
}
