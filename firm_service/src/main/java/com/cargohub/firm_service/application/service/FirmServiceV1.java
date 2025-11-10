package com.cargohub.firm_service.application.service;

import com.cargohub.firm_service.application.command.CreateFirmCommandV1;
import com.cargohub.firm_service.domain.entity.Firm;
import com.cargohub.firm_service.domain.port.HubValidatorPort;
import com.cargohub.firm_service.domain.repository.FirmRepository;
import com.cargohub.firm_service.domain.vo.HubId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FirmServiceV1 {

    private final FirmRepository firmRepository;
    private final HubValidatorPort hubValidatorPort;

    @Transactional
    public Firm createFirm(CreateFirmCommandV1 command) {

        // HubId 생성
        HubId hubId = HubId.of(command.hubId());

        // Hub 유효성 검증
        if(!hubValidatorPort.validateHub(hubId)){
            throw new IllegalArgumentException("유효하지 않는 허브 ID 입니다." + hubId.getHubId());
        }

        // Firm 생성 후 저장
        Firm firm = Firm.ofNewFirm(
          command.name(),
          command.type(),
          hubId,
          command.address()
        );

        return firmRepository.save(firm);
    }
}
