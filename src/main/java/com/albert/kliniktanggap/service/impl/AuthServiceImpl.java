package com.albert.kliniktanggap.service.impl;

import com.albert.kliniktanggap.dto.request.LoginRequest;
import com.albert.kliniktanggap.dto.response.AuthResponse;
import com.albert.kliniktanggap.entity.User;
import com.albert.kliniktanggap.entity.PasswordResetToken;
import com.albert.kliniktanggap.repository.UserRepository;
import com.albert.kliniktanggap.repository.PasswordResetTokenRepository;
import com.albert.kliniktanggap.service.AuthService;
import com.albert.kliniktanggap.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email atau password salah"));

        boolean validPassword = passwordEncoder.matches(request.getPassword(), user.getPassword())
                || user.getPassword().equals(request.getPassword());
        if (!validPassword) {
            throw new RuntimeException("Email atau password salah");
        }

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        return AuthResponse.builder()
                .token(UUID.randomUUID().toString())
                .type("Bearer")
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    public AuthResponse me(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return AuthResponse.builder()
                .token("dummy")
                .type("Bearer")
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    @Transactional
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email tidak terdaftar"));

        // Hapus token lama jika ada untuk user tersebut
        passwordResetTokenRepository.deleteByUser(user);

        // Buat token baru
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .build();

        passwordResetTokenRepository.save(resetToken);

        // Kirim email
        emailService.sendResetPasswordEmail(user.getEmail(), token);
    }

    @Override
    public void validateResetToken(String token) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token reset password tidak valid"));

        if (resetToken.isExpired()) {
            throw new RuntimeException("Token reset password telah kedaluwarsa");
        }
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token reset password tidak valid"));

        if (resetToken.isExpired()) {
            throw new RuntimeException("Token reset password telah kedaluwarsa");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Hapus token setelah berhasil digunakan
        passwordResetTokenRepository.delete(resetToken);
    }
}
