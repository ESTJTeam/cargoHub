package hub_server.hub_server.application.dto.command;

import java.util.UUID;

public record DeleteHubCommandV1(
	UUID hubId
) {
}
