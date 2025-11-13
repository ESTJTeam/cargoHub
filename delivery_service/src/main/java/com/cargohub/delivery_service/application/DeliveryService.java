package com.cargohub.delivery_service.application;

import com.cargohub.delivery_service.application.dto.request.DeliveryRequestV1;
import com.cargohub.delivery_service.application.dto.response.DeliveryResponseV1;
import com.cargohub.delivery_service.domain.repository.FirmDeliveryRepository;
import com.cargohub.delivery_service.domain.repository.HubDeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryService {

//    private final FirmClient firmClient;
//    private final HubClient hubClient;

    private final FirmDeliveryRepository firmDeliveryRepository;
    private final HubDeliveryRepository hubDeliveryRepository;

    @Transactional
    public DeliveryResponseV1 createDelivery(DeliveryRequestV1 request) {

//        HubRouteResponseV1 hubRoute = hubClient.getRouteBetweenHubs(request.getFromHubId(), request.getToHubId());

        return new DeliveryResponseV1(
            request.getOrderId(),
            request.getFromHubId(),
            request.getToHubId(),
            request.getDestinationId(),
            request.getReceiverSlackId()
        );
    }
}
