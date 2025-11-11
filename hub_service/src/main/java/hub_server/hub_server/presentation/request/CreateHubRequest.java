package hub_server.hub_server.presentation.request;

import hub_server.hub_server.application.dto.command.CreateHubCommandV1;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateHubRequest(
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
    public CreateHubCommandV1 toCommand() {
        return new CreateHubCommandV1(
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

/**
 서울특별시 센터 : 서울특별시 송파구 송파대로 55 위도(Latitude) : 37.4742027808565 / 경도(Longitude) : 127.123621185562

 경기 북부 센터 : 경기도 고양시 덕양구 권율대로 570 위도(Latitude) : 37.6403771056018 / 경도(Longitude) : 126.87379545786

 경기 남부 센터 : 경기도 이천시 덕평로 257-21 위도(Latitude) : 37.1896213142136 / 경도(Longitude) : 127.375050006958

 부산광역시 센터 : 부산 동구 중앙대로 206 위도(Latitude) : 35.117605126596 / 경도(Longitude) : 129.045060216345


 대구광역시 센터 : 대구 북구 태평로 161 위도(Latitude) : 35.8758849492106 / 경도(Longitude) : 128.596129208483


 인천광역시 센터 : 인천 남동구 정각로 29 위도(Latitude) : 37.4560499608337 / 경도(Longitude) : 126.705255744089


 광주광역시 센터 : 광주 서구 내방로 111 위도(Latitude) : 35.1600994105234 / 경도(Longitude) : 126.851461925213


 대전광역시 센터 : 대전 서구 둔산로 100 위도(Latitude) : 36.3503849976553 / 경도(Longitude) : 127.384633005948


 울산광역시 센터 : 울산 남구 중앙로 201 위도(Latitude) : 35.5390270962011 / 경도(Longitude) : 129.311356392207


 세종특별자치시 센터 : 세종특별자치시 한누리대로 2130 위도(Latitude) : 36.4800579897497 / 경도(Longitude) : 127.289039408864


 강원특별자치도 센터 : 강원특별자치도 춘천시 중앙로 1 위도(Latitude) : 37.8800729197963 / 경도(Longitude) : 127.727907820318


 충청북도 센터 : 충북 청주시 상당구 상당로 82 위도(Latitude) : 36.6353867908159 / 경도(Longitude) : 127.491428436987


 충청남도 센터 : 충남 홍성군 홍북읍 충남대로 21 위도(Latitude) : 36.6590416999343 / 경도(Longitude) : 126.673057036952


 전북특별자치도 센터 : 전북특별자치도 전주시 완산구 효자로 225 위도(Latitude) : 35.8194621650578 / 경도(Longitude) : 127.106396942356


 전라남도 센터 : 전남 무안군 삼향읍 오룡길 1 위도(Latitude) : 34.8174727676363 / 경도(Longitude) : 126.465415935304


 경상북도 센터 : 경북 안동시 풍천면 도청대로 455 위도(Latitude) : 36.5761205474728 / 경도(Longitude) : 128.505722686385


 경상남도 센터 : 경남 창원시 의창구 중앙대로 300 위도(Latitude) : 35.2378032514675 / 경도(Longitude) : 128.691940442146

 */
