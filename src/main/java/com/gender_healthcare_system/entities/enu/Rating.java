package com.gender_healthcare_system.entities.enu;

public enum Rating {
    EXCELLENT,
    GOOD,
    AVERAGE,
    POOR;

    public String getRating() {
        return this.name();
    }
}

