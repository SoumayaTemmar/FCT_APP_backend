package com.soumaya.FctApp.backend.appControllers.periode;

import com.soumaya.FctApp.backend.Exceptions.OperationNotPermittedException;
import com.soumaya.FctApp.backend.Periode.Periode;
import com.soumaya.FctApp.backend.Periode.PeriodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.Year;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PeriodMapper {
    private final PeriodeRepository repository;

    public PeriodResponse toPeriodResponse(Periode periode){

        return PeriodResponse.builder()
                .id(periode.getId())
                .month(periode.getMonth())
                .year(periode.getYear())
                .monthYear(periode.getMonthYear())
                .opened(periode.isOpened())
                .build();
    }

    public Periode toPeriod(PeriodRequest request){
        String monthYear;
        Month month;

        try {
            month = Month.valueOf(request.month().toUpperCase());
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("invalid month");
        }

        if (request.year() != Year.now().getValue()){
            throw new IllegalArgumentException("invalid year");
        }
        monthYear = month + Integer.toString(request.year());
        return Periode.builder()
                .month(month)
                .year(request.year())
                .monthYear(monthYear)
                .deleted(false)
                .opened(request.opened())
                .build();
    }

    public Periode updateMyPeriod(Periode periode, PeriodRequest request){
        String monthYear;
        Month month;

        try {
            month = Month.valueOf(request.month().toUpperCase());
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("invalid month");
        }

        if (request.year() != Year.now().getValue()){
            throw new IllegalArgumentException("invalid year");
        }
        monthYear = month + Integer.toString(request.year());

        if(!Objects.equals(periode.getMonthYear(), monthYear)){
            if (repository.existsByMonthYear(monthYear)){
                throw new OperationNotPermittedException(
                        "this period: "+monthYear+" already exist or was not deleted permanently");
            }
        }

        periode.setMonth(month);
        periode.setYear(request.year());
        periode.setMonthYear(monthYear);
        periode.setOpened(request.opened());

        return periode;
    }
}
