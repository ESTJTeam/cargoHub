package hub_server.hub_server.presentation.request;

import hub_server.hub_server.application.dto.query.HubSearchCondition;

public record HubSearchRequest(
        String name,
        String region,
        String city,
        String sortBy,      // createdAt, updatedAt
        String sortDirection // asc, desc
) {
    public HubSearchCondition toCondition() {
        return HubSearchCondition.builder()
                .name(name)
                .region(region)
                .city(city)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();
    }
}
