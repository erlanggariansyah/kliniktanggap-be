package com.albert.kliniktanggap.controller;

import com.albert.kliniktanggap.dto.ApiResponse;
import com.albert.kliniktanggap.dto.request.LoginRequest;
import com.albert.kliniktanggap.dto.response.AuthResponse;
import com.albert.kliniktanggap.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Login berhasil", authService.login(request)));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthResponse>> me(@RequestParam String email) {
        return ResponseEntity.ok(ApiResponse.ok(authService.me(email)));
    }
}
