package com.cargohub.firm_service.domain.port;

import com.cargohub.firm_service.domain.vo.HubId;

public interface HubValidatorPort {

    boolean validateHub(HubId hubId);
}
