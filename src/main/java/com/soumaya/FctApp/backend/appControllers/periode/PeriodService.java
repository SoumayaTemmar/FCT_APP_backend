package com.soumaya.FctApp.backend.appControllers.periode;

import com.soumaya.FctApp.backend.Exceptions.OperationNotPermittedException;
import com.soumaya.FctApp.backend.Periode.Periode;
import com.soumaya.FctApp.backend.Periode.PeriodeRepository;
import com.soumaya.FctApp.backend.common.PageResponse;
import com.soumaya.FctApp.backend.common.StandardResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PeriodService {
    private final PeriodeRepository periodeRepository;
    private final PeriodMapper periodMapper;


    @Transactional
    public StandardResponse addPeriod(PeriodRequest request){

        Periode periode = periodMapper.toPeriod(request);

        if (periodeRepository.existsByMonthYear(periode.getMonthYear())){
            throw new OperationNotPermittedException(
                    "this period "+periode.getMonthYear()+ " already exist or was not deleted permanently");
        }

        return StandardResponse.builder()
                .id( periodeRepository.save(periode).getId())
                .message("period added successfully")
                .build();
    }

    public PageResponse<PeriodResponse> getAllPeriods(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate"));
        Page<Periode> periods = periodeRepository.findAllByDeletedFalse(pageable);

        List<PeriodResponse> periodResponses = periods.stream()
                .map(periodMapper::toPeriodResponse)
                .toList();

        return new PageResponse<>(
                periodResponses,
                periods.getNumber(),
                periods.getSize(),
                periods.getTotalPages(),
                periods.getTotalElements()
        );
    }

    public PageResponse<PeriodResponse> getOpenedPeriods(int page, int size){
        Pageable pageable = PageRequest.of(page, size,Sort.by("monthYear"));
        Page<Periode> periodes = periodeRepository.findAllByOpenedTrueAndDeletedFalse(pageable);

        List<PeriodResponse> periodResponses = periodes.stream()
                .map(periodMapper::toPeriodResponse)
                .toList();

        return new PageResponse<>(
                periodResponses,
                periodes.getNumber(),
                periodes.getSize(),
                periodes.getTotalPages(),
                periodes.getTotalElements()
        );
    }

    @Transactional
    public StandardResponse updatePeriod(int id, PeriodRequest request){
        Periode periode = periodeRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("period with id: "+id+" does not exist"));


        periodeRepository.save(periodMapper.updateMyPeriod(periode, request));
        return StandardResponse.builder()
                .id(periode.getId())
                .message("period updated successfully")
                .build();
    }

    public PeriodResponse getPeriodById(int id) {
        Periode periode = periodeRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "la periode avec le id: "+id+" n'existe pas"
                ));

        return periodMapper.toPeriodResponse(periode);
    }

    @Transactional
    public StandardResponse closePeriod(int id){
        Periode periode = periodeRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("period with id: "+id+" does not exist"));

        if (periode.isDeleted()){
            throw new IllegalStateException("cannot close deleted period! please restore it first");
        }

        if (!periode.isOpened()){
            throw new IllegalStateException("period already closed!");
        }
        periode.setOpened(false);
        periodeRepository.save(periode);

        return StandardResponse.builder()
                .id(id)
                .message("period closed successfully")
                .build();
    }

    @Transactional
    public StandardResponse openPeriod(int id){
        Periode periode = periodeRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("period with id: "+id+" does not exist"));

        if (periode.isDeleted()){
            throw new IllegalStateException("cannot open deleted period! please restore it first");
        }

        if (periode.isOpened()){
            throw new IllegalStateException("period already opened!");
        }
        periode.setOpened(true);
        periodeRepository.save(periode);

        return StandardResponse.builder()
                .id(id)
                .message("period opened successfully")
                .build();

    }

    public PageResponse<PeriodResponse> getDeletedPeriods(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("lastModifiedDate"));
        Page<Periode> periods = periodeRepository.findAllByDeletedTrue(pageable);

        List<PeriodResponse> periodResponses = periods.stream()
                .map(periodMapper::toPeriodResponse)
                .toList();

        return new PageResponse<>(
                periodResponses,
                periods.getNumber(),
                periods.getSize(),
                periods.getTotalPages(),
                periods.getTotalElements()
        );
    }

    @Transactional
    public StandardResponse softDelete(int id){
        Periode periode = periodeRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "period with id: "+id+" does not exist"
                ));

        if (periode.isDeleted()){
            throw new IllegalStateException("period already deleted,please check the trash");
        }
        periode.setDeleted(true);
        periodeRepository.save(periode);

        return StandardResponse.builder()
                .id(id)
                .message("period deleted successfully!")
                .build();
    }

    @Transactional
    public StandardResponse restorePeriod(int id){
        Periode periode = periodeRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "period with id: "+id+" does not exist"
                ));

        if (!periode.isDeleted()){
            throw new IllegalStateException("only deleted periods can be restored");
        }
        periode.setDeleted(false);
        periodeRepository.save(periode);

        return StandardResponse.builder()
                .id(id)
                .message("period restored successfully!")
                .build();

    }
    @Transactional
    public StandardResponse deletePeriod(int id){
        Periode periode = periodeRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "period with id: "+id+" does not exist"
                ));

        periodeRepository.delete(periode);
        return StandardResponse.builder()
                .id(id)
                .message("period was deleted permanently!")
                .build();
    }

}
