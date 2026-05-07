package com.albert.kliniktanggap.enums;

public enum Gender {
    MALE("Laki-laki"),
    FEMALE("Perempuan");

    private final String label;

    Gender(String label) { this.label = label; }
    public String getLabel() { return label; }
}
