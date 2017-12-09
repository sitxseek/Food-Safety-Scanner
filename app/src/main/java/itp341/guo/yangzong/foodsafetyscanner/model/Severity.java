package itp341.guo.yangzong.foodsafetyscanner.model;

/**
 * Created by yangzong on 11/14/17.
 */

public enum Severity {
    LIFE_THREATENING ("LIFE-THREATENING"),
    MODERATE ("MODERATE"),
    MILD ("MILD");

    private String severity;
    private Severity(String severity) {
        this.severity = severity;
    }

    @Override
    public String toString(){
        return severity;
    }
}