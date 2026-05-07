package com.albert.kliniktanggap.service;

import com.albert.kliniktanggap.dto.request.LoginRequest;
import com.albert.kliniktanggap.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse me(String email);
}
