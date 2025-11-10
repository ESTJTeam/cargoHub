package com.cargohub.order_service.domain.repository;

import com.cargohub.order_service.domain.entity.Order;

public interface OrderRepository {

    Order save (Order order);

}
