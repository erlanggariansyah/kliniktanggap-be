package com.albert.kliniktanggap.enums;

public enum PriorityLevel {
    LOW("Rendah"),
    MEDIUM("Sedang"),
    HIGH("Tinggi");

    private final String label;

    PriorityLevel(String label) { this.label = label; }
    public String getLabel() { return label; }
}
