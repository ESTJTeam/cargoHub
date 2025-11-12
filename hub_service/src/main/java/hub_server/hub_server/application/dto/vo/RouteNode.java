package hub_server.hub_server.application.dto.vo;

import java.util.UUID;

/**
 * 다익스트라 알고리즘에서 사용하는 노드 정보
 * 우선순위 큐에서 비교를 위해 Comparable 구현
 */
public record RouteNode(
        UUID hubId,
        Integer duration,
        Double distance
) implements Comparable<RouteNode> {

    @Override
    public int compareTo(RouteNode other) {
        // 소요 시간 기준으로 오름차순 정렬 (최소 힙)
        return Integer.compare(this.duration, other.duration);
    }
}
