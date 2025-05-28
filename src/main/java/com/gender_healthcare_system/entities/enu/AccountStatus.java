package com.gender_healthcare_system.entities.enu;


public enum AccountStatus {
    ACTIVE,
    INACTIVE,
    SUSPENDED;

    public String getStatus() {
        return this.name();
    }
}
