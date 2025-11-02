package com.soumaya.FctApp.backend.appControllers.fct;

import com.soumaya.FctApp.backend.ActFct.ActFct;
import com.soumaya.FctApp.backend.ActFct.ActFctMapper;
import com.soumaya.FctApp.backend.Exceptions.OperationNotPermittedException;
import com.soumaya.FctApp.backend.Fct.Fct;
import com.soumaya.FctApp.backend.Fct.FctRepository;
import com.soumaya.FctApp.backend.Periode.Periode;
import com.soumaya.FctApp.backend.Periode.PeriodeRepository;
import com.soumaya.FctApp.backend.Unite.Unite;
import com.soumaya.FctApp.backend.User.User;
import com.soumaya.FctApp.backend.common.PageResponse;
import com.soumaya.FctApp.backend.common.StandardResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FctService {
    private final PeriodeRepository periodeRepository;
    private final FctRepository fctRepository;
    private final FctMapper fctMapper;
    private final ActFctMapper actFctMapper;

    @Transactional
    public FctCreatedResponse addFct(FctRequest request, Authentication connectedUser) {

        List<ActFct> actFCTs = request.actFctRequests().stream()
                .map(actFctMapper::toActFct)
                .collect(Collectors.toCollection(ArrayList::new));

        Periode periode = periodeRepository.findByMonthYearAndDeletedFalse(request.monthYear())
                .orElseThrow(() -> new EntityNotFoundException(
                        "no such period: " + request.monthYear()
                ));
        Unite unite = ((User) connectedUser.getPrincipal()).getUnite();

        if (!periode.isOpened()) {
            throw new IllegalStateException("period is not opened yet!");
        }
        Fct fct = Fct.builder()
                .periode(periode)
                .owner((User) connectedUser.getPrincipal())
                .unite(unite)
                .deleted(false)
                .build();

        actFCTs.forEach(actFct -> actFct.setFct(fct));
        fct.setActFcts(actFCTs);

        return FctCreatedResponse.builder()
                .id(fctRepository.save(fct).getId())
                .message("Fct added successfully")
                .build();
    }

    // get all FCTs ------> admin only
    public PageResponse<FctResponse> getAllFcts(int page , int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate"));
        Page<Fct> fcts = fctRepository.findAllByPeriodOpened(pageable);

        List<FctResponse> fctResponses = fcts.stream()
                .map(fctMapper::toFctResponse)
                .toList();

        return new PageResponse<>(
                fctResponses,
                fcts.getNumber(),
                fcts.getSize(),
                fcts.getTotalPages(),
                fcts.getTotalElements()
        );

    }
    // update an fct
    @Transactional
    public UpdateResponse updateFct(
            int id,
            FctRequest request,
            Authentication connectedUser
    ){
        Fct fct = fctRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "fct with id: "+ id + " does not exist"));

        User user = (User) connectedUser.getPrincipal();

        if (!Objects.equals(fct.getOwner().getUsername(),user.getUsername())){
            throw new AccessDeniedException("you don't have permission to perform this action");
        }

        if (fct.isDeleted()){
            throw new IllegalStateException("you cannot update deleted fct");
        }

        //fctRepository.save(fctMapper.updateMyFct(fct, request, connectedUser));

        log.debug("Before saving FCT with activities: {}", request.actFctRequests().size());
        log.debug("Period: {}", fct.getPeriode().getMonthYear());
        log.debug("FCT object: {}", fct);

        try {
            fctRepository.save(fctMapper.updateMyFct(fct, request, connectedUser));
        } catch (Exception e) {
            log.error("Saving FCT failed!", e);
            throw e;
        }

        return UpdateResponse.builder()
                .id(fct.getId())
                .message("fct updated successfully")
                .build();
    }

    // soft delete
    @Transactional
    public StandardResponse softDeleteFct(int id, Authentication connectedUser){
        Fct fct = fctRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("fct with id: "+ id+ " does not exist")
        );

        User user = (User) connectedUser.getPrincipal();

        if (!Objects.equals(fct.getOwner().getUsername(),user.getUsername())){
            throw new AccessDeniedException("you don't have permission to perform this action");
        }

        if (fct.isDeleted()){
            throw new IllegalStateException("fct already deleted");
        }

        fct.setDeleted(true);
        fctRepository.save(fct);
        return StandardResponse.builder()
                .id(id)
                .message("fct was delete successfully").build();
    }
    // restore fct
    @Transactional
    public StandardResponse restoreFct(int id, Authentication connectedUser){
        Fct fct = fctRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("fct with id: "+ id+ " does not exist")
        );
        User user = (User) connectedUser.getPrincipal();

        if (!Objects.equals(fct.getOwner().getUsername(),user.getUsername())){
            throw new AccessDeniedException("you don't have permission to perform this action");
        }

        if (!fct.isDeleted()){
            throw new IllegalStateException("fct already restored!");
        }

        fct.setDeleted(false);
        fctRepository.save(fct);
        return StandardResponse.builder()
                .id(id)
                .message("fct was restored successfully").build();
    }
    // delete fct permanently
    @Transactional
    public StandardResponse deleteFct(int id, Authentication connectedUser){
        Fct fct = fctRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("fct with id: "+ id+ " does not exist")
        );
        User user = (User) connectedUser.getPrincipal();

        if (!Objects.equals(fct.getOwner().getUsername(),user.getUsername())){
            throw new AccessDeniedException("you don't have permission to perform this action");
        }

        fctRepository.delete(fct);
        return StandardResponse.builder()
                .id(id)
                .message("fct was deleted permanently").build();
    }
    // get all deleted fcts
    public PageResponse<FctResponse> getDeletedFcts(int page, int size, Authentication connectedUser){

        Pageable pageable = PageRequest.of(page, size, Sort.by("lastModifiedDate"));
        User user = (User) connectedUser.getPrincipal();
        Page<Fct> fcts = fctRepository.findAllByOwnerAndDeletedTrue(pageable, user);

        List<FctResponse> fctResponses = fcts.stream()
                .map(fctMapper::toFctResponse)
                .toList();

        return new PageResponse<>(
                fctResponses,
                fcts.getNumber(),
                fcts.getSize(),
                fcts.getTotalPages(),
                fcts.getTotalElements()
        );
    }
    // get non deleted Fcts
    public PageResponse<FctResponse> getFctOfUser(int page, int size, Authentication connectedUser){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate"));

        Page<Fct> fcts = fctRepository.findAllByOwnerAndDeletedFalse(pageable,(User)connectedUser.getPrincipal());
        List<FctResponse> fctResponses = fcts.stream()
                .map(fctMapper::toFctResponse)
                .toList();

        return new PageResponse<>(
                fctResponses,
                fcts.getNumber(),
                fcts.getSize(),
                fcts.getTotalPages(),
                fcts.getTotalElements()
        );
    }

    //admin can Delete any fct
    @Transactional
    public StandardResponse softDeleteByAdmin(int id){
        Fct fct = fctRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("fct with id: " + id + " does not exist")
        );

        if (fct.isDeleted()){
            throw new IllegalStateException("fct already deleted");
        }
        fct.setDeleted(true);
        fctRepository.save(fct);
        return StandardResponse.builder()
                .id(id)
                .message("fct deleted successfully").build();
    }
    //admin can restore deleted fct
    @Transactional
    public StandardResponse restoreFctByAdmin(int id){
        Fct fct = fctRepository.findById(id).orElseThrow(
                ()->  new EntityNotFoundException("fct with id: " + id + " does not exist")
        );

        if (!fct.isDeleted()){
            throw new IllegalStateException("only deleted Fcts can be restored");
        }

        fct.setDeleted(false);
        fctRepository.save(fct);
        return StandardResponse.builder()
                .id(id)
                .message("fct restored successfully").build();
    }
    // admin can get all deleted fcts
    public PageResponse<FctResponse>  getAllSoftDeletedFctAdmin(
            int page,
            int size
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by("lastModifiedDate"));
        Page<Fct> fcts = fctRepository.findAllByDeletedTrue(pageable);

        List<FctResponse> fctResponses = fcts.stream().map(fctMapper::toFctResponse)
                .toList();

        return new PageResponse<>(
                fctResponses,
                fcts.getNumber(),
                fcts.getSize(),
                fcts.getTotalPages(),
                fcts.getTotalElements()
        );
    }
    // admin can delete permanently any fct
    @Transactional
    public StandardResponse deleteFctPermanentlyAdmin(int id){
        Fct fct = fctRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(
                        "fct with id: " + id + " does not exist"
                ));

        fctRepository.delete(fct);
        return StandardResponse.builder()
                .id(id)
                .message("fct deleted successfully").build();
    }

    // get Fct by id
    public FctResponse getFctById(int id, Authentication connectedUser){
        Fct fct = fctRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException(
                        "fct with id: "+ id + "does not exist"
                )
        );

        User user = (User) connectedUser.getPrincipal();
        if (!Objects.equals(fct.getOwner().getUsername(), user.getUsername())){
            throw new OperationNotPermittedException(
                    "You can only get your FCTs"
            );
        }

        return fctMapper.toFctResponse(fct);
    }
    // admin can get others FCTs
    public FctResponse getFctByIdAdmin(int id){
        Fct fct = fctRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException(
                        "fct with id: "+ id + "does not exist"
                )
        );

        return fctMapper.toFctResponse(fct);
    }

}






