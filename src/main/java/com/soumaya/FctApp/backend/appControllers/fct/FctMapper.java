package com.soumaya.FctApp.backend.appControllers.fct;

import com.soumaya.FctApp.backend.ActFct.ActFct;
import com.soumaya.FctApp.backend.ActFct.ActFctMapper;
import com.soumaya.FctApp.backend.ActFct.ActFctResponse;
import com.soumaya.FctApp.backend.Fct.Fct;
import com.soumaya.FctApp.backend.Periode.Periode;
import com.soumaya.FctApp.backend.Periode.PeriodeRepository;
import com.soumaya.FctApp.backend.Unite.Unite;
import com.soumaya.FctApp.backend.User.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FctMapper {

    private final PeriodeRepository periodeRepository;
    private final ActFctMapper actFctMapper;

    public FctResponse toFctResponse(Fct fct){

        HashMap<String, String> unite = new HashMap<>();
        unite.put("code", fct.getUnite().getCode());
        unite.put("imputation", fct.getUnite().getImputation());
        unite.put("DenominationFr", fct.getUnite().getDenominationFr());

        List<ActFctResponse> fctResponseList = fct.getActFcts().stream()
                .map(actFctMapper::toActFctResponse)
                .toList();

        return FctResponse.builder()
                .id(fct.getId())
                .ownerUsername(fct.getOwner().getUsername())
                .mat(fct.getOwner().getMat())
                .actFctResponseList(fctResponseList)
                .periodMonthYear(fct.getPeriode().getMonthYear())
                .unite(unite)
                .build();
    }

    public Fct updateMyFct(Fct fct, FctRequest request, Authentication connectedUser){

        if (!fct.getPeriode().getMonthYear().equals(request.monthYear())){
            Periode periode = periodeRepository.findByMonthYearAndDeletedFalse(request.monthYear())
                    .orElseThrow(()->new EntityNotFoundException("no such period was found"));

            fct.setPeriode(periode);
        }

        List<ActFct> actFcts = request.actFctRequests().stream()
                .map(actFctMapper::toActFct)
                .collect(Collectors.toCollection(ArrayList::new));

        actFcts.forEach(actFct -> actFct.setFct(fct));

        Unite unite = ((User)connectedUser.getPrincipal()).getUnite();

        fct.setUnite(unite);

        fct.getActFcts().clear();               // remove old ones because we are using orphan removal
        fct.getActFcts().addAll(actFcts);      // add new ones

        return fct;
    }
}
