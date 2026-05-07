package com.albert.kliniktanggap.enums;

public enum Severity {
    MILD("ringan", 2.0),
    MODERATE("sedang", 7.0),
    SEVERE("berat", 16.0);

    private final String label;
    private final double baseScore;

    Severity(String label, double baseScore) {
        this.label = label;
        this.baseScore = baseScore;
    }
    public String getLabel() { return label; }
    public double getBaseScore() { return baseScore; }
}
