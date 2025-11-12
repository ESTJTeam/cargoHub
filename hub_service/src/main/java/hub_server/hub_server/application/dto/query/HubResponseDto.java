package hub_server.hub_server.application.dto.query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record HubResponseDto(
	UUID id,
	String name,
	UUID hubManagerId,
	HubAddressDto address,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public record HubAddressDto(
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
	) {}
}
