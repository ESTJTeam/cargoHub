package com.cargohub.order_service.domain.repository;

import com.cargohub.order_service.application.command.SearchOrderCommandV1;
import com.cargohub.order_service.domain.entity.Order;
import com.cargohub.order_service.domain.vo.FirmDeliveryId;
import com.cargohub.order_service.domain.vo.HubDeliveryId;
import com.cargohub.order_service.domain.vo.ReceiverId;
import com.cargohub.order_service.domain.vo.SupplierId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Order save (Order order);

    Optional<Order> findById(UUID id);

    /**
     * todo: 권한 별 조회 필요 -
     * 1. 마스터 조회 - 전체 주문 조회
     * 2. 허브 관리자 조회 - 담당 허브 주문 조회
     * 3. 업체 담당자 조회 - 본인 주문 조회
     * 4. 배송 담당자 - 본인 주문 조회
     */

    // 마스터 - 전체 주문 조회
    Page<Order> findOrderPage(SearchOrderCommandV1 search, Pageable pageable);

    // 허브 관리자 - 담당 공급 업체 주문 조회
    Page<Order> findOrderPageBySupplierId(SupplierId supplierId, SearchOrderCommandV1 search, Pageable pageable);
    Page<Order> findOrderPageByFirmIdIn(Collection<UUID> firmIds, SearchOrderCommandV1 search, Pageable pageable);

    // 업체 담당자
    Page<Order> findOrderPageByReceiverId(ReceiverId receiverId, SearchOrderCommandV1 search, Pageable pageable);
    Page<Order> findOrderPageByFirmId(UUID firmId, SearchOrderCommandV1 search, Pageable pageable);

    // 허브 배송 담당자
    Page<Order> findOrderPageByHubDeliveryId(HubDeliveryId hubDeliveryId, SearchOrderCommandV1 search, Pageable pageable);

    // 업체 배송 담당자
    Page<Order> findOrderPageByFirmDeliveryId(FirmDeliveryId firmDeliveryId, SearchOrderCommandV1 search, Pageable pageable);

}
