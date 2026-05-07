package com.albert.kliniktanggap.enums;

public enum Duration {
    LESS_THAN_1_DAY("<1 hari", 1.0),
    ONE_TO_THREE_DAYS("1-3 hari", 4.0),
    THREE_TO_SEVEN_DAYS("3-7 hari", 7.0),
    MORE_THAN_7_DAYS(">7 hari", 10.0);

    private final String label;
    private final double baseScore;

    Duration(String label, double baseScore) {
        this.label = label;
        this.baseScore = baseScore;
    }
    public String getLabel() { return label; }
    public double getBaseScore() { return baseScore; }
}
