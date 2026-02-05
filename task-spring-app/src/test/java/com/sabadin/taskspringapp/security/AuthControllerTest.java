package com.sabadin.taskspringapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sabadin.taskspringapp.security.controller.AuthController;
import com.sabadin.taskspringapp.security.jwt.JwtAuthenticationFilter;
import com.sabadin.taskspringapp.security.jwt.JwtService;
import com.sabadin.taskspringapp.security.model.dto.AuthResponse;
import com.sabadin.taskspringapp.security.model.dto.RegisterRequest;
import com.sabadin.taskspringapp.security.model.entity.Role;
import com.sabadin.taskspringapp.security.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@WebMvcTest(AuthController.class)
//@AutoConfigureMockMvc(addFilters = false)
@Import({JwtService.class, JwtAuthenticationFilter.class})
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

//    @Autowired
//    private ObjectMapper objectMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser
    void register_ShouldReturnAuthResponse_WhenValidRequest() throws Exception {
        // Arrange
        RegisterRequest request = RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .middleName("Michael")
                .email("john.doe@example.com")
                .logonName("johndoe")
                .password("Password123!")
                .role(Role.ROLE_USER)
                .build();
        AuthResponse authResponse = new AuthResponse("jwt-token-123");
        when(authService.register(any(RegisterRequest.class)))
                .thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token-123"));
    }
}
