package cargohub.orchestratorservice.infrastructure.client.user.dto.response;


import cargohub.orchestratorservice.domain.Role;

public record UserResponseV1(

    String slackId,
    Role role,
    String username,
    String nickname,
    String email

){ }
