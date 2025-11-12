package user_server.user_server.domain.vo;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import user_server.user_server.libs.error.BusinessException;
import user_server.user_server.libs.error.ErrorCode;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubId {

    private UUID hubId;

    public HubId(UUID hubId) {
        if (hubId == null) {
            throw new BusinessException(ErrorCode.INVALID_HUB_ID);
        }
        this.hubId = hubId;
    }

    public static HubId fromString(UUID hubId) {
        return new HubId(hubId);
    }
}
