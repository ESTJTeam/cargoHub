package hub_server.hub_server.application.dto.query;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HubSearchCondition {
	private String name;        // 허브명 검색 (like)
	private String region;      // 지역 검색
	private String city;        // 도시 검색
	private String sortBy;      // 정렬 기준 (createdAt, updatedAt)
	private String sortDirection; // 정렬 방향 (asc, desc)
}
