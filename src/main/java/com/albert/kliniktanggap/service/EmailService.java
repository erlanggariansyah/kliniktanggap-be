package com.albert.kliniktanggap.service;

public interface EmailService {
    void sendResetPasswordEmail(String toEmail, String token);
}
