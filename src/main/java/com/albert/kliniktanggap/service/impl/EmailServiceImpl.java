package com.albert.kliniktanggap.service.impl;

import com.albert.kliniktanggap.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Override
    public void sendResetPasswordEmail(String toEmail, String token) {
        String resetUrl = "http://localhost:5173/reset-password?token=" + token;
        String subject = "Reset Password - Klinik Tanggap";
        String content = "Halo,\n\n"
                + "Anda menerima email ini karena ada permintaan untuk mereset password akun Anda di Klinik Tanggap.\n\n"
                + "Silakan klik link di bawah ini untuk mereset password Anda:\n"
                + resetUrl + "\n\n"
                + "Link ini akan kedaluwarsa dalam waktu 15 menit.\n\n"
                + "Jika Anda tidak meminta reset password, silakan abaikan email ini.\n\n"
                + "Salam,\nTim Klinik Tanggap";

        log.info("=== SIMULASI EMAIL RESET PASSWORD ===");
        log.info("Tujuan Email : {}", toEmail);
        log.info("Subjek       : {}", subject);
        log.info("Link Reset   : {}", resetUrl);
        log.info("======================================");

        if (mailSender != null) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(toEmail);
                message.setSubject(subject);
                message.setText(content);
                mailSender.send(message);
                log.info("Email berhasil dikirim melalui SMTP server.");
            } catch (Exception e) {
                log.error("Gagal mengirim email melalui SMTP: {}. (Silakan periksa kredensial SMTP Gmail Anda di application.properties)", e.getMessage());
            }
        } else {
            log.warn("JavaMailSender belum terinisialisasi. Pastikan properti spring.mail dikonfigurasi dengan benar di application.properties.");
        }
    }
}
