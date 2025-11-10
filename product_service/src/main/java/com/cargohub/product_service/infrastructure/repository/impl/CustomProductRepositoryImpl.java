package com.cargohub.product_service.infrastructure.repository.impl;

import com.cargohub.product_service.application.command.SearchProductCommandV1;
import com.cargohub.product_service.domain.entity.Product;
import com.cargohub.product_service.domain.entity.QProduct;
import com.cargohub.product_service.domain.vo.FirmId;
import com.cargohub.product_service.domain.vo.HubId;
import com.cargohub.product_service.infrastructure.repository.CustomProductRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
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

public class CustomProductRepositoryImpl extends QuerydslRepositorySupport implements CustomProductRepository {

    private final JPAQueryFactory queryFactory;
    private final QProduct qProduct = QProduct.product;

    public CustomProductRepositoryImpl(EntityManager em) {
        super(Product.class);
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Product> findProductPage(SearchProductCommandV1 param, Pageable pageable) {
        return findProductPageByCondition(param, pageable, null);
    }

    @Override
    public Page<Product> findProductPageByHubId(HubId hubId, SearchProductCommandV1 param, Pageable pageable) {
        return findProductPageByCondition(param, pageable, qProduct.hubId.eq(hubId));
    }

    @Override
    public Page<Product> findProductPageByFirmId(FirmId firmId, SearchProductCommandV1 param, Pageable pageable) {
        return findProductPageByCondition(param, pageable, qProduct.firmId.eq(firmId));
    }

    public Page<Product> findProductPageByCondition(SearchProductCommandV1 param, Pageable pageable, @Nullable Predicate additionalCondition) {

        BooleanBuilder where = whereExpression(param);
        where.and(qProduct.deletedAt.isNull());

        if (additionalCondition != null) {
            where.and(additionalCondition);
        }

        JPAQuery<Product> query = queryFactory
                .select(qProduct)
                .from(qProduct)
                .where(where)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order order : pageable.getSort()) {
            PathBuilder<Object> entityPath = new PathBuilder<>(Product.class, "product");
            query.orderBy(new OrderSpecifier(
                    order.isAscending() ? Order.ASC : Order.DESC,
                    entityPath.get(order.getProperty())));
        }

        List<Product> results = query.fetch();

        JPAQuery<Long> total = queryFactory
                .select(qProduct.count())
                .from(qProduct)
                .where(where);

        return PageableExecutionUtils.getPage(results, pageable, total::fetchOne);
    }


    private BooleanBuilder whereExpression(SearchProductCommandV1 param) {
        BooleanBuilder where = new BooleanBuilder();

        if (param.name() != null) {
            where.and(qProduct.name.containsIgnoreCase(param.name()));
        }
        if (param.firmId() != null) {
            where.and(qProduct.firmId.id.eq(param.firmId()));
        }
        if (param.hubId() != null) {
            where.and(qProduct.hubId.id.eq(param.hubId()));
        }
        if (param.sellable() != null) {
            where.and(qProduct.sellable.eq(param.sellable()));
        }

        return where;
    }
}
