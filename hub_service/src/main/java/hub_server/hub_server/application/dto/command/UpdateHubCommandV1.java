package hub_server.hub_server.application.dto.command;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateHubCommandV1(
	UUID hubId,
	String name,
	String postalCode,
	String country,
	String region,
	String city,
	String district,
	String roadName,
	String buildingName,
	String detailAddress,
	String fullAddress,
	BigDecimal latitude,
	BigDecimal longitude
) {
}
