package com.gender_healthcare_system.payloads.todo;

import com.gender_healthcare_system.entities.enu.Rating;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultationEvaluatePayload {

    private Rating rating = Rating.EXCELLENT;

    @Nationalized
    @Nullable
    @Size(min = 5,max = 255, message = "Comment must be empty or between 5 and 255 characters")
    private String comment;
}
