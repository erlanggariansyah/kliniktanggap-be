package com.albert.kliniktanggap.enums;

public enum UserRole {
    PETUGAS("Petugas"),
    DOKTER("Dokter"),
    ADMIN("Admin");

    private final String label;

    UserRole(String label) { this.label = label; }
    public String getLabel() { return label; }
}
