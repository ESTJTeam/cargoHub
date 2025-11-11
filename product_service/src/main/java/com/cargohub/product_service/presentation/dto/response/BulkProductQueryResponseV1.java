package com.cargohub.product_service.presentation.dto.response;

import java.util.Map;
import java.util.UUID;

public record BulkProductQueryResponseV1(
        Map<UUID, ReadProductSummaryResponseV1> products
) {
    public static BulkProductQueryResponseV1 from(Map<UUID, ReadProductSummaryResponseV1> products) {
        return new BulkProductQueryResponseV1(products);
    }
}
