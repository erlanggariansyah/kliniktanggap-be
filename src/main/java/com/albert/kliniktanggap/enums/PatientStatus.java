package com.albert.kliniktanggap.enums;

public enum PatientStatus {
    WAITING("Menunggu"),
    IN_PROGRESS("Dilayani"),
    COMPLETED("Selesai"),
    REFERRED("Dirujuk");

    private final String label;

    PatientStatus(String label) { this.label = label; }
    public String getLabel() { return label; }
}
