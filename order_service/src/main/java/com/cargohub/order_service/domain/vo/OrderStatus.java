package com.cargohub.order_service.domain.vo;


public enum OrderStatus {

    /** 상품이 출고 준비 중인 상태 */
    PREPARING("출고준비중"),

    /** 배송이 시작된 상태 (배송 엔티티 생성 후 연동) */
    IN_DELIVERY("배송중"),

    /** 수령 업체가 주문을 수령 완료한 상태 */
    DELIVERED("납품완료"),

    /** 주문이 취소된 상태 (논리삭제 처리) */
    CANCELLED("주문취소");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    public String getCode() {
        return name();
    }

    public String getLabel() {
        return label;
    }

}
