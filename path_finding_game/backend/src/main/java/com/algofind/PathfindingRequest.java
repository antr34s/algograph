package com.algofind;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
public class PathfindingRequest {
    public enum Algorithm {
        DFS,
        BFS,
        DIJKSTRA,
        A_STAR
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Point {
        private int x;
        private int y;
    }
    private int gridSize;
    private Point start;
    private Point end;
    private List<Point> barriers;
    private Algorithm algorithm;
    private boolean allowDiagonal;

}
