package com.gender_healthcare_system.payloads;

import com.gender_healthcare_system.entities.enu.TestingServiceHistoryStatus;  // Enum trạng thái (đổi theo project)
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * DTO nhận dữ liệu cập nhật cho TestingServiceHistory.
 * Dùng kèm @Validated ở tầng Service/Controller để đảm bảo dữ liệu hợp lệ trước khi gọi repo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestingServiceHistoryPayload {

    /** Kết quả xét nghiệm (có thể null nếu chưa có) */
    @Size(max = 255, message = "Result tối đa 255 ký tự")
    private String result;

    /** Điểm đánh giá 1–5 sao */
    @Min(value = 1, message = "Rating min is 1 star")
    @Max(value = 5, message = "Rating max is 5 stars")
    private Integer rating;

    /** Ghi chú của khách hàng hoặc nhân viên */
    @Length(max = 500)
    private String comment;

    /** Trạng thái (PENDING, COMPLETED, CANCELLED, …) */
    @NotNull(message = "Status cannot be null")
    private TestingServiceHistoryStatus status;
}
