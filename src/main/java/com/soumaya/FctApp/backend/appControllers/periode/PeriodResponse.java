package com.soumaya.FctApp.backend.appControllers.periode;

import jakarta.persistence.Column;
import lombok.*;

import java.time.Month;
import java.time.Year;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PeriodResponse {
    private int id;
    private Month month;
    private int year;
    private boolean opened;
    private String monthYear;
}
