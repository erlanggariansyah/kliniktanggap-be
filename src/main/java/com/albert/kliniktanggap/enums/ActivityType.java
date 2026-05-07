package com.albert.kliniktanggap.enums;

public enum ActivityType {
    INPUT("Input Pasien"),
    COMPLETED("Selesai"),
    REFERRED("Rujukan"),
    CONFIGURATION("Konfigurasi"),
    LOGIN("Login");

    private final String label;

    ActivityType(String label) { this.label = label; }
    public String getLabel() { return label; }
}
