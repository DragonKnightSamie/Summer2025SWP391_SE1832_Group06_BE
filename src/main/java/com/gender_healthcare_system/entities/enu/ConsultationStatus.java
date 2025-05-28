package com.gender_healthcare_system.entities.enu;

public enum ConsultationStatus {
    CANCELLED,
    COMPLETED,
    SCHEDULED,
    CONFIRMED,
    PENDING;

    public String getStatus() {
        return this.name();
    }
}
