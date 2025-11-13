package user_server.user_server.application.service;


import java.util.UUID;

public interface HubClient {

    boolean validateHub(UUID hubId);

}
