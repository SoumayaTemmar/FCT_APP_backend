package com.soumaya.FctApp.backend.appControllers.Unite;

import com.soumaya.FctApp.backend.Unite.Unite;
import org.springframework.stereotype.Service;

@Service
public class UniteMapper {

    public UniteResponse toUniteResponse(Unite unite){
        return UniteResponse.builder()
                .code(unite.getCode())
                .denominationFr(unite.getDenominationFr())
                .id(unite.getId())
                .imputation(unite.getImputation())
                .build();
    }

    public Unite toUnite(UniteRequest request) {
        return Unite.builder()
                .code(request.getCode())
                .imputation(request.getImputation())
                .denominationFr(request.getDenominationFr())
                .deleted(false)
                .build();
    }
}
