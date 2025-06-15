package com.gender_healthcare_system.entities.enu;

public enum TestingServiceBookingStatus {
    PENDING,
    CONFIRMED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
    ;

    public String getTestingServiceHistoryStatus() {
        return this.name();
    }
}
