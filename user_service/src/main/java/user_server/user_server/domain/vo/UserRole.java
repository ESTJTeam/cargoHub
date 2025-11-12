package user_server.user_server.domain.vo;

import user_server.user_server.domain.entity.Role;
import user_server.user_server.libs.error.BusinessException;
import user_server.user_server.libs.error.ErrorCode;

public enum UserRole {
    DELIVERY_MANAGER, SUPPLIER_MANAGER;


    public static UserRole fromRole(Role string) {
        if (string == null) {
            throw new BusinessException(ErrorCode.INVALID_USER_ROLE);
        }
        return UserRole.valueOf(String.valueOf(string));

    }
}
