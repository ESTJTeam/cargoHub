package hub_server.hub_server.application.dto.command;

import java.math.BigDecimal;

public record CreateHubCommandV1(
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
