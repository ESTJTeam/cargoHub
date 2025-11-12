package hub_server.hub_server.presentation.request;

import hub_server.hub_server.application.dto.command.UpdateHubCommandV1;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateHubRequest(
        @NotBlank(message = "허브명은 필수입니다.")
        String name,
        String postalCode,
        String country,
        String region,
        String city,
        String district,
        String roadName,
        String buildingName,
        String detailAddress,
        @NotBlank(message = "전체 주소는 필수입니다.")
        String fullAddress,
        @NotNull(message = "위도는 필수입니다.")
        BigDecimal latitude,
        @NotNull(message = "경도는 필수입니다.")
        BigDecimal longitude
) {
    public UpdateHubCommandV1 toCommand(UUID hubId) {
        return new UpdateHubCommandV1(
                hubId,
                name,
                postalCode,
                country,
                region,
                city,
                district,
                roadName,
                buildingName,
                detailAddress,
                fullAddress,
                latitude,
                longitude
        );
    }
}
