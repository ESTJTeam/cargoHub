package com.cargohub.order_service.domain.vo;

import lombok.Getter;

@Getter
public enum OrderStatus {

    /** 상품이 출고 준비 중인 상태 */
    PREPARING("출고준비중") {
        @Override
        public boolean canTransitionTo(OrderStatus newStatus) {
            return newStatus == SHIPPED || newStatus == CANCELLED;
        }

        @Override
        public boolean canBeCancelled() {
            return true;
        }
    },

    /** 배송이 시작된 상태 (배송 엔티티 생성 후 연동) */
    SHIPPED("배송중") {
        @Override
        public boolean canTransitionTo(OrderStatus newStatus) {
            return newStatus == DELIVERED;
        }

        @Override
        public boolean canBeCancelled() {
            return false;
        }
    },

    /** 수령 업체가 주문을 수령 완료한 상태 */
    DELIVERED("배송 완료") {
        @Override
        public boolean canTransitionTo(OrderStatus newStatus) {
            return false;
        }

        @Override
        public boolean canBeCancelled() {
            return false;
        }
    },

    /** 주문이 취소된 상태 (논리삭제 처리) */
    CANCELLED("주문 취소") {
        @Override
        public boolean canTransitionTo(OrderStatus newStatus) {
            return false;
        }

        @Override
        public boolean canBeCancelled() {
            return false;
        }
    };

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    public String getCode() {
        return name();
    }

    // 상태 전이 가능 여부 체크
    public abstract boolean canTransitionTo(OrderStatus newStatus);

    // 취소 가능 여부
    public abstract boolean canBeCancelled();

}
