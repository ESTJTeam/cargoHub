package com.cargohub.firm_service.application.service;

import com.cargohub.firm_service.application.command.CreateFirmCommandV1;
import com.cargohub.firm_service.application.command.UpdateFirmCommandV1;
import com.cargohub.firm_service.domain.entity.Firm;
import com.cargohub.firm_service.domain.port.HubValidatorPort;
import com.cargohub.firm_service.domain.repository.FirmRepository;
import com.cargohub.firm_service.domain.vo.HubId;
import com.cargohub.firm_service.domain.vo.UserId;
import com.cargohub.firm_service.presentation.dto.response.FirmDetailResponseV1;
import com.cargohub.firm_service.presentation.dto.response.FirmListResponseV1;
import com.cargohub.firm_service.presentation.dto.response.FirmSummaryV1;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FirmServiceV1 {

    private final FirmRepository firmRepository;
    private final HubValidatorPort hubValidatorPort;

    @Transactional(readOnly = true)
    public FirmListResponseV1 getFirmsByHubId(UUID hubIdValue, int page, int size) {

        HubId hubId = HubId.of(hubIdValue);

        PageRequest pageable = PageRequest.of(page - 1, size);

        Page<Firm> result = firmRepository.findByHubId(hubId, pageable);

        List<FirmSummaryV1> firms = result.getContent().stream()
                .map(FirmSummaryV1::from)
                .toList();

        return new FirmListResponseV1(
                hubIdValue,
                page,
                size,
                result.getTotalElements(),
                firms
        );
    }

    @Transactional(readOnly = true)
    public FirmDetailResponseV1 getFirm(UUID firmId) {

        Firm firm = firmRepository.findById(firmId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 업체입니다. id=" + firmId));

        return FirmDetailResponseV1.from(firm);
    }

    @Transactional
    public void createFirm(CreateFirmCommandV1 command) {

        // HubId 생성
        HubId hubId = HubId.of(command.hubId());

        // Hub 유효성 검증
        if(!hubValidatorPort.validateHub(hubId)){
            throw new IllegalArgumentException("유효하지 않는 허브 ID 입니다." + hubId.getHubId());
        }

        // UserId 생성
        UserId userId = UserId.of(command.userId());

        // UserId 유효성 검증 (추후 추가 예정)

        // Firm 생성 후 저장
        Firm firm = Firm.ofNewFirm(
          command.name(),
          command.type(),
          hubId,
          userId,
          command.address()
        );

        firmRepository.save(firm);
    }

    @Transactional
    public void updateFirm(UpdateFirmCommandV1 command) {

        UUID firmId = command.firmId();

        // 기존 엔티티 조회
        Firm firm = firmRepository.findById(command.firmId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 업체입니다. id=" + command.firmId()));

        // HubId VO 변환 + 검증
        HubId hubId = HubId.of(command.hubId());
        if (!hubValidatorPort.validateHub(hubId)) {
            throw new IllegalArgumentException("유효하지 않은 허브 ID입니다: " + hubId.getHubId());
        }

        // UserId VO 변환
        UserId userId = UserId.of(command.userId());

        // UserId 검증 (추후 추가 예정)

        // 엔티티에 값 반영
        firm.update(
                command.name(),
                command.type(),
                hubId,
                userId,
                command.address()
        );
    }

    @Transactional
    public void deleteFirm(UUID firmId, UUID userId) {
        Firm firm = firmRepository.findById(firmId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 업체입니다. id=" + firmId));

        firm.deleteFirm(userId); // BaseEntity.delete(userId) 호출
    }
}
