package com.gender_healthcare_system.entities.enu;

import java.util.List;

public enum ConsultationType {
    GENERAL_HEALTH,
    GYNECOLOGY,
    UROLOGY,
    SEXUAL_HEALTH,
    MENSTRUAL_HEALTH;

    public String getStatus(){
        return this.name();
    }

}
