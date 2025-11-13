package hub_server.hub_server.application.service;

import hub_server.hub_server.application.dto.vo.RouteNode;
import hub_server.hub_server.application.dto.vo.ShortestPathResult;
import hub_server.hub_server.domain.entity.HubInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 다익스트라 알고리즘을 사용한 최단 경로 계산 서비스
 * 허브 간 최소 시간 경로를 계산합니다.
 */
@Slf4j
@Service
public class DijkstraRouteCalculator {

    /**
     * 그래프의 간선 정보
     */
    private static class Edge {
        UUID toHubId;
        Integer duration;
        Double distance;

        Edge(UUID toHubId, Integer duration, Double distance) {
            this.toHubId = toHubId;
            this.duration = duration;
            this.distance = distance;
        }
    }

    /**
     * 다익스트라 알고리즘을 사용하여 모든 허브 쌍의 최단 경로 계산
     *
     * @param allHubIds 모든 허브 ID 리스트
     * @param hubInfos  직접 연결된 허브 정보 리스트
     * @return 모든 허브 쌍의 최단 경로 결과 리스트
     */
    public List<ShortestPathResult> calculateAllShortestPaths(
            List<UUID> allHubIds,
            List<HubInfo> hubInfos
    ) {
        log.info("Calculating all shortest paths for {} hubs", allHubIds.size());

        // 1. 그래프 구축 (인접 리스트)
        Map<UUID, List<Edge>> graph = buildGraph(hubInfos);

        // 2. 모든 허브 쌍에 대해 최단 경로 계산
        List<ShortestPathResult> results = new ArrayList<>();
        for (UUID startHubId : allHubIds) {
            // 출발지에서 다른 모든 허브까지의 최단 경로 계산
            Map<UUID, ShortestPathResult> shortestPaths = dijkstra(startHubId, allHubIds, graph);

            // 결과 수집 (자기 자신으로의 경로는 제외)
            for (UUID endHubId : allHubIds) {
                if (!startHubId.equals(endHubId)) {
                    ShortestPathResult result = shortestPaths.get(endHubId);
                    if (result != null) {
                        results.add(result);
                    } else {
                        log.warn("No path found from {} to {}", startHubId, endHubId);
                    }
                }
            }
        }

        log.info("Calculated {} shortest paths", results.size());
        return results;
    }

    /**
     * 특정 출발지에서 특정 도착지까지의 최단 경로 계산
     *
     * @param startHubId 출발 허브 ID
     * @param endHubId   도착 허브 ID
     * @param hubInfos   직접 연결된 허브 정보 리스트
     * @return 최단 경로 결과
     */
    public ShortestPathResult calculateShortestPath(
            UUID startHubId,
            UUID endHubId,
            List<HubInfo> hubInfos
    ) {
        log.info("Calculating shortest path from {} to {}", startHubId, endHubId);

        Map<UUID, List<Edge>> graph = buildGraph(hubInfos);
        Map<UUID, ShortestPathResult> shortestPaths = dijkstra(startHubId, List.of(endHubId), graph);

        return shortestPaths.get(endHubId);
    }

    /**
     * 그래프 구축 (인접 리스트 방식)
     *
     * @param hubInfos 직접 연결된 허브 정보 리스트
     * @return 그래프 (Map<출발허브ID, List<간선정보>>)
     */
    private Map<UUID, List<Edge>> buildGraph(List<HubInfo> hubInfos) {
        Map<UUID, List<Edge>> graph = new HashMap<>();

        for (HubInfo info : hubInfos) {
            UUID startId = info.getStartHub().getId();
            UUID endId = info.getEndHub().getId();

            // 양방향 그래프 구축 (A->B 연결이 있으면 B->A도 가능)
            graph.computeIfAbsent(startId, k -> new ArrayList<>())
                    .add(new Edge(endId, info.getDeliveryDuration(), info.getDistance()));

            graph.computeIfAbsent(endId, k -> new ArrayList<>())
                    .add(new Edge(startId, info.getDeliveryDuration(), info.getDistance()));
        }

        return graph;
    }

    /**
     * 다익스트라 알고리즘 구현
     * 출발지에서 다른 모든 허브까지의 최단 경로를 계산합니다.
     *
     * @param startHubId 출발 허브 ID
     * @param targetHubIds 계산 대상 허브 ID 리스트 (최적화용)
     * @param graph 그래프
     * @return Map<도착허브ID, 최단경로결과>
     */
    private Map<UUID, ShortestPathResult> dijkstra(
            UUID startHubId,
            List<UUID> targetHubIds,
            Map<UUID, List<Edge>> graph
    ) {
        // 최단 거리 저장 (허브 ID -> 최소 소요 시간)
        Map<UUID, Integer> distances = new HashMap<>();
        distances.put(startHubId, 0);

        // 누적 거리 저장
        Map<UUID, Double> totalDistances = new HashMap<>();
        totalDistances.put(startHubId, 0.0);

        // 이전 노드 추적 (경로 복원용)
        Map<UUID, UUID> previous = new HashMap<>();

        // 우선순위 큐 (소요 시간 기준 최소 힙)
        PriorityQueue<RouteNode> pq = new PriorityQueue<>();
        pq.offer(new RouteNode(startHubId, 0, 0.0));

        // 방문 여부 체크
        Set<UUID> visited = new HashSet<>();

        while (!pq.isEmpty()) {
            RouteNode current = pq.poll();
            UUID currentHubId = current.hubId();

            // 이미 방문한 노드는 스킵
            if (visited.contains(currentHubId)) {
                continue;
            }
            visited.add(currentHubId);

            // 현재 허브에서 연결된 모든 인접 허브 탐색
            List<Edge> edges = graph.getOrDefault(currentHubId, Collections.emptyList());
            for (Edge edge : edges) {
                UUID nextHubId = edge.toHubId;

                // 이미 방문한 노드는 스킵
                if (visited.contains(nextHubId)) {
                    continue;
                }

                // 현재 경로를 통한 소요 시간 계산
                int newDuration = distances.get(currentHubId) + edge.duration;
                double newDistance = totalDistances.get(currentHubId) + edge.distance;

                // 더 짧은 경로를 발견한 경우 업데이트
                if (!distances.containsKey(nextHubId) || newDuration < distances.get(nextHubId)) {
                    distances.put(nextHubId, newDuration);
                    totalDistances.put(nextHubId, newDistance);
                    previous.put(nextHubId, currentHubId);
                    pq.offer(new RouteNode(nextHubId, newDuration, newDistance));
                }
            }
        }

        // 결과 생성
        Map<UUID, ShortestPathResult> results = new HashMap<>();
        for (UUID targetHubId : targetHubIds) {
            if (distances.containsKey(targetHubId)) {
                List<UUID> path = reconstructPath(previous, startHubId, targetHubId);
                results.put(targetHubId, new ShortestPathResult(
                        startHubId,
                        targetHubId,
                        distances.get(targetHubId),
                        totalDistances.get(targetHubId),
                        path
                ));
            }
        }

        return results;
    }

    /**
     * 경로 복원
     * previous 맵을 역추적하여 출발지부터 도착지까지의 경로를 복원합니다.
     *
     * @param previous 이전 노드 추적 맵
     * @param startHubId 출발 허브 ID
     * @param endHubId 도착 허브 ID
     * @return 경로 (허브 ID 리스트)
     */
    private List<UUID> reconstructPath(Map<UUID, UUID> previous, UUID startHubId, UUID endHubId) {
        List<UUID> path = new ArrayList<>();
        UUID current = endHubId;

        // 역순으로 경로 추적
        while (current != null) {
            path.add(current);
            current = previous.get(current);
        }

        // 경로를 역순으로 뒤집기 (출발지 -> 도착지 순서)
        Collections.reverse(path);

        return path;
    }
}
