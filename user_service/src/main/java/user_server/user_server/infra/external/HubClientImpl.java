package user_server.user_server.infra.external;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import user_server.user_server.application.service.HubClient;

@Component
@RequiredArgsConstructor
public class HubClientImpl implements HubClient {

    private final HubFeign hubfeign;


    @Override
    public boolean validateHub(UUID hubId) {
        hubfeign.validateHub(hubId);
        return true;
    }
}
