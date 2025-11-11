package com.example.backend.dto.request;

import com.example.backend.constant.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ReservationFilterDTO {
    private String query;

    private String username;

    private ReservationStatus reservationStatus;

    private Boolean isPaid;

    private BigDecimal totalAmount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String sortBy = "newest";

    private String sortDirection = "asc";


    @AssertTrue(message = "Start date must be before or equal to end date")
    public boolean isValidDateRange() {
        if (startDate == null || endDate == null) {
            return true;
        }
        return !startDate.isAfter(endDate);
    }

    @AssertTrue(message = "Sort direction must be 'asc' or 'desc'")
    public boolean isSortDirectionValid() {
        return sortDirection == null || sortDirection.equalsIgnoreCase("asc") || sortDirection.equalsIgnoreCase("desc");
    }

}
