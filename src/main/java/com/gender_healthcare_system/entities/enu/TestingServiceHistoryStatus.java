package com.gender_healthcare_system.entities.enu;

public enum TestingServiceHistoryStatus {
    CANCELLED,
    COMPLETED,
    IN_PROGRESS,
    SCHEDULED,
    CONFIRMED,
    PENDING;

    public String getTestingServiceHistoryStatus() {
        return this.name();
    }
}
