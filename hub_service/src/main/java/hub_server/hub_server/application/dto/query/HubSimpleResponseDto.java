package hub_server.hub_server.application.dto.query;

import java.util.UUID;

public record HubSimpleResponseDto(
	UUID id,
	String name,
	String region,
	String city,
	String fullAddress
) {}
