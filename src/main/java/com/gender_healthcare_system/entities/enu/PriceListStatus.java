package com.gender_healthcare_system.entities.enu;

public enum PriceListStatus {
    ACTIVE,
    INACTIVE,
    DELETED;

    public String getStatus(){
        return this.name();
    }
}
