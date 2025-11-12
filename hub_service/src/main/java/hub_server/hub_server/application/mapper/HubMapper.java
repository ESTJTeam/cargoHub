package hub_server.hub_server.application.mapper;

import java.util.UUID;

import hub_server.hub_server.application.dto.command.CreateHubCommandV1;
import hub_server.hub_server.application.dto.query.HubResponseDto;
import hub_server.hub_server.application.dto.query.HubSimpleResponseDto;
import hub_server.hub_server.domain.entity.Hub;
import hub_server.hub_server.domain.entity.HubAddress;
import org.springframework.stereotype.Component;

@Component
public class HubMapper {

	/**
	 * Hub 엔티티를 HubResponseDto로 변환
	 */
	public HubResponseDto toResponseDto(Hub hub) {
		if (hub == null) {
			return null;
		}

		HubResponseDto.HubAddressDto addressDto = null;
		if (hub.getHubAddress() != null) {
			addressDto = toAddressDto(hub.getHubAddress());
		}

		return new HubResponseDto(
			hub.getId(),
			hub.getName(),
			hub.getHubManagerId(),
			addressDto,
			hub.getCreatedAt(),
			hub.getUpdatedAt()
		);
	}

	/**
	 * Hub 엔티티를 HubSimpleResponseDto로 변환
	 */
	public HubSimpleResponseDto toSimpleResponseDto(Hub hub) {
		if (hub == null) {
			return null;
		}

		String region = null;
		String city = null;
		String fullAddress = null;

		if (hub.getHubAddress() != null) {
			region = hub.getHubAddress().getRegion();
			city = hub.getHubAddress().getCity();
			fullAddress = hub.getHubAddress().getFullAddress();
		}

		return new HubSimpleResponseDto(
			hub.getId(),
			hub.getName(),
			region,
			city,
			fullAddress
		);
	}

	/**
	 * HubAddress 엔티티를 HubAddressDto로 변환
	 */
	private HubResponseDto.HubAddressDto toAddressDto(HubAddress address) {
		return new HubResponseDto.HubAddressDto(
			address.getPostalCode(),
			address.getCountry(),
			address.getRegion(),
			address.getCity(),
			address.getDistrict(),
			address.getRoadName(),
			address.getBuildingName(),
			address.getDetailAddress(),
			address.getFullAddress(),
			address.getLatitude(),
			address.getLongitude()
		);
	}

	/**
	 * CreateHubCommandV1으로부터 Hub 엔티티 생성
	 */
	public Hub toEntity(CreateHubCommandV1 command, UUID hubManagerId) {
		Hub hub = Hub.create(command.name(), hubManagerId);
		HubAddress address = HubAddress.create(
			command.postalCode(),
			command.country(),
			command.region(),
			command.city(),
			command.district(),
			command.roadName(),
			command.buildingName(),
			command.detailAddress(),
			command.fullAddress(),
			command.latitude(),
			command.longitude(),
			hub
		);
		hub.setHubAddress(address);
		return hub;
	}
}
