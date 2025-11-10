package com.cargohub.order_service.infrastructure.repository.impl;

import com.cargohub.order_service.application.command.SearchOrderCommandV1;
import com.cargohub.order_service.domain.entity.Order;
import com.cargohub.order_service.domain.entity.QOrder;
import com.cargohub.order_service.domain.vo.FirmDeliveryId;
import com.cargohub.order_service.domain.vo.HubDeliveryId;
import com.cargohub.order_service.domain.vo.ReceiverId;
import com.cargohub.order_service.domain.vo.SupplierId;
import com.cargohub.order_service.infrastructure.repository.CustomOrderRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

public class CustomOrderRepositoryImpl extends QuerydslRepositorySupport implements CustomOrderRepository {

    private final JPAQueryFactory queryFactory;
    private final QOrder qOrder = QOrder.order;
    private final PathBuilder<Order> orderPath = new PathBuilder<>(Order.class, "order");

    public CustomOrderRepositoryImpl(EntityManager em) {
        super(Order.class);
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Order> findOrderPage(SearchOrderCommandV1 param, Pageable pageable) {

        BooleanBuilder where = new BooleanBuilder();

        if (param.supplierId() != null) {
            where.and(qOrder.supplierId.id.eq(param.supplierId()));
        }

        return findOrderPageByCondition(param, pageable, where);
    }

    @Override
    public Page<Order> findOrderPageBySupplierId(SupplierId supplierId, SearchOrderCommandV1 param, Pageable pageable) {
        return findOrderPageByCondition(param, pageable, qOrder.supplierId.eq(supplierId));
    }

    @Override
    public Page<Order> findOrderPageByReceiverId(ReceiverId receiverId, SearchOrderCommandV1 param, Pageable pageable) {
        return findOrderPageByCondition(param, pageable, qOrder.receiverId.eq(receiverId));
    }

    @Override
    public Page<Order> findOrderPageByHubDeliveryId(HubDeliveryId hubDeliveryId, SearchOrderCommandV1 param, Pageable pageable) {
        return findOrderPageByCondition(param, pageable, qOrder.hubDeliveryId.eq(hubDeliveryId));
    }

    @Override
    public Page<Order> findOrderPageByFirmDeliveryId(FirmDeliveryId firmDeliveryId, SearchOrderCommandV1 param, Pageable pageable) {
        return findOrderPageByCondition(param, pageable, qOrder.firmDeliveryId.eq(firmDeliveryId));
    }

    private Page<Order> findOrderPageByCondition(SearchOrderCommandV1 param, Pageable pageable, @Nullable Predicate additionalCondition) {

        BooleanBuilder where = whereExpression(param);

        if (additionalCondition != null) {
            where.and(additionalCondition);
        }

        JPAQuery<Order> query = queryFactory
                .select(qOrder)
                .from(qOrder)
                .where(where)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order order : pageable.getSort()) {
            query.orderBy(new OrderSpecifier(
                    order.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC,
                    orderPath.get(order.getProperty())
            ));
        }

        List<Order> results = query.fetch();

        JPAQuery<Long> total = queryFactory
                .select(qOrder.count())
                .from(qOrder)
                .where(where);

        return PageableExecutionUtils.getPage(results, pageable, total::fetchOne);
    }

    private BooleanBuilder whereExpression(SearchOrderCommandV1 search) {

        BooleanBuilder where = new BooleanBuilder();

        if (search.receiverId() != null) {
            where.and(qOrder.receiverId.id.eq(search.receiverId()));
        }

        if (search.status() != null) {
            where.and(qOrder.status.eq(search.status()));
        }

        if (search.createdBy() != null) {
            where.and(qOrder.createdBy.eq(search.createdBy()));
        }

        if (search.requestNote() != null && !search.requestNote().isEmpty()) {
            where.and(qOrder.requestNote.containsIgnoreCase(search.requestNote()));
        }

        if (search.startDate() != null) {
            where.and(qOrder.createdAt.goe(search.startDate()));
        }

        if (search.endDate() != null) {
            where.and(qOrder.createdAt.loe(search.endDate()));
        }

        return where;
    }
}
