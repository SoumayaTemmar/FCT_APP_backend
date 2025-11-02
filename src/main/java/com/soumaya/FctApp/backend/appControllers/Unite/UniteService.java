package com.soumaya.FctApp.backend.appControllers.Unite;


import com.soumaya.FctApp.backend.Exceptions.OperationNotPermittedException;
import com.soumaya.FctApp.backend.Unite.Unite;
import com.soumaya.FctApp.backend.Unite.UniteRepository;
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
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UniteService {
    private final UniteRepository uniteRepository;
    private final UniteMapper uniteMapper;

    public PageResponse<UniteResponse> getAllUnities(int page, int size){

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate"));
        Page<Unite> unites = uniteRepository.findAllByDeletedFalse(pageable);

        List<UniteResponse> uniteResponses = unites.stream()
                .map(uniteMapper::toUniteResponse)
                .toList();

        return new PageResponse<>(
                uniteResponses,
                unites.getNumber(),
                unites.getSize(),
                unites.getTotalPages(),
                unites.getTotalElements()
        );
    }

    public UniteResponse getUniteById(int id){
        Unite unite = uniteRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("unité avec id: "+id +" n'existe pas")
        );

        return uniteMapper.toUniteResponse(unite);
    }
    @Transactional
    public StandardResponse addUnite(UniteRequest request) {
        if (uniteRepository.existsByImputation(request.getImputation())){
            throw new OperationNotPermittedException(
                    "Une unité avec une même imputation existe déjà,(ou pas supprimée définitivement), veuillez choisir une autre"
            );
        }
        if (uniteRepository.existsByDenominationFr(request.getDenominationFr())){
            throw new OperationNotPermittedException(
                    "Une unité avec une même denomination existe déjà,(ou pas supprimée définitivement), veuillez choisir une autre"
            );
        }

        int id = uniteRepository.save(uniteMapper.toUnite(request)).getId();

        return StandardResponse.builder()
                .id(id)
                .message("unite Ajoutée!")
                .build();
    }

    // soft delete
    @Transactional
    public StandardResponse softDeleteUnite(int id){
        Unite unite = uniteRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("unité avec id: "+id +" n'existe pas")
        );

        if (unite.isDeleted()){
            throw new IllegalStateException("unite est déjà supprimée");
        }
        unite.getEmployees().forEach(emp-> {
            emp.setDeleted(true);
            emp.getFct().forEach(fct -> fct.setDeleted(true));
        });

        unite.setDeleted(true);

        uniteRepository.save(unite);
        return StandardResponse.builder()
                .id(id)
                .message("unité supprimée!")
                .build();
    }

    @Transactional
    public StandardResponse restoreUnite(int id) {
        Unite unite = uniteRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException(
                        "unité avec id: "+id +" n'existe pas"
                )
        );

        if (!unite.isDeleted()){
            throw new IllegalStateException("unité est déjà restorée");
        }

        unite.getEmployees().forEach(emp-> {
            emp.setDeleted(false);
            emp.getFct().forEach(fct-> fct.setDeleted(false));
        });

        unite.setDeleted(false);

        return StandardResponse.builder()
                .id(id)
                .message("unité restorée!")
                .build();
    }

    public PageResponse<UniteResponse> getTrash(int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("lastModifiedDate"));
        Page<Unite> unites = uniteRepository.findAllByDeletedTrue(pageable);

        List<UniteResponse> uniteResponses = unites.stream()
                .map(uniteMapper::toUniteResponse)
                .toList();

        return new PageResponse<>(
                uniteResponses,
                unites.getNumber(),
                unites.getSize(),
                unites.getTotalPages(),
                unites.getTotalElements()
        );
    }

    // delete and update

    @Transactional
    public StandardResponse deleteUnite(int id){
        Unite unite = uniteRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException(
                        "unité avec id: "+id +" n'existe pas"
                )
        );

        uniteRepository.delete(unite);
        return StandardResponse.builder()
                .id(id).message("unité supprimée définitivement ").build();
    }

    @Transactional
    public StandardResponse updateUnite(int id, UniteRequest request){
        Unite unite = uniteRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException(
                        "unité avec id: "+id +" n'existe pas"
                )
        );
        if (!Objects.equals(unite.getImputation(), request.getImputation())){
            if (uniteRepository.existsByImputation(request.getImputation())){
                throw new OperationNotPermittedException(
                        "unité avec imputation: "+ request.getImputation()+" existe déjà; ou pas supprimée définitivement "
                );
            }
        }

        if (!Objects.equals(unite.getDenominationFr(), request.getDenominationFr())){
            if (uniteRepository.existsByDenominationFr(request.getDenominationFr())){
                throw new OperationNotPermittedException(
                        "unité avec la denomination: "+ request.getDenominationFr()+" existe déjà; ou pas supprimée définitivement "
                );
            }
        }
        if (unite.isDeleted()){
            throw new IllegalStateException("vous ne pouvez pas modifier des Unités supprimée, veuillez verifier la corbeille");
        }

        unite.setCode(request.getCode());
        unite.setImputation(request.getImputation());
        unite.setDenominationFr(request.getDenominationFr());
        uniteRepository.save(unite);

        return StandardResponse.builder()
                .id(id)
                .message("unité modifiée").build();
    }
}
