package com.cargohub.order_service.infrastructure.repository;

import com.cargohub.order_service.application.command.SearchOrderCommandV1;
import com.cargohub.order_service.domain.entity.Order;
import com.cargohub.order_service.domain.vo.FirmDeliveryId;
import com.cargohub.order_service.domain.vo.HubDeliveryId;
import com.cargohub.order_service.domain.vo.ReceiverId;
import com.cargohub.order_service.domain.vo.SupplierId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomOrderRepository {

    Page<Order> findOrderPage(SearchOrderCommandV1 searchOrder, Pageable pageable);

    Page<Order> findOrderPageBySupplierId(SupplierId supplierId, SearchOrderCommandV1 searchOrder, Pageable pageable);

    Page<Order> findOrderPageByReceiverId(ReceiverId receiverId, SearchOrderCommandV1 searchOrder, Pageable pageable);

    Page<Order> findOrderPageByHubDeliveryId(HubDeliveryId hubDeliveryId, SearchOrderCommandV1 searchOrder, Pageable pageable);

    Page<Order> findOrderPageByFirmDeliveryId(FirmDeliveryId firmDeliveryId, SearchOrderCommandV1 searchOrder, Pageable pageable);
}
