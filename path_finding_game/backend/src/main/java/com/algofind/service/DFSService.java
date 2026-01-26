package com.algofind.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.algofind.PathfindingRequest;
import com.algofind.PathfindingRequest.Point;
import com.algofind.PathfindingResponse;

@Service
public class DFSService implements PathfindingService {

    private static final int[][] DIRECTIONS_4 = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    private static final int[][] DIRECTIONS_8 = {
        {0, 1}, {1, 0}, {0, -1}, {-1, 0},
        {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
    };
    private final Random random = new Random();
    private int nodesExplored;
    private List<Point> visitedPath;
    private boolean allowDiagonal;

    @Override
    public PathfindingResponse execute(PathfindingRequest request) {
        int gridSize = request.getGridSize();
        Point start = request.getStart();
        Point end = request.getEnd();
        Set<Point> barriers = new HashSet<>(request.getBarriers() != null ? request.getBarriers() : new ArrayList<>());
        allowDiagonal = request.isAllowDiagonal();

        Set<Point> visited = new HashSet<>();
        Map<Point, Point> parent = new HashMap<>();
        visitedPath = new ArrayList<>();
        nodesExplored = 0;

        visitedPath.add(start);
        boolean found = dfs(start, end, gridSize, barriers, visited, parent);

        if (found) {
            List<Point> path = reconstructPath(parent, start, end);
            return new PathfindingResponse(path, visitedPath, nodesExplored, 0, true, getAlgorithmName());
        }

        return new PathfindingResponse(new ArrayList<>(), visitedPath, nodesExplored, 0, false, getAlgorithmName());
    }

    @Override
    public String getAlgorithmName() {
        return "DFS";
    }

    private boolean dfs(Point current, Point end, int gridSize, Set<Point> barriers,
                       Set<Point> visited, Map<Point, Point> parent) {
        if (current.equals(end)) {
            nodesExplored++;
            return true;
        }

        visited.add(current);
        nodesExplored++;

        List<Point> neighbors = getNeighbors(current, gridSize, barriers, visited, allowDiagonal);

        for (Point neighbor : neighbors) {
            parent.put(neighbor, current);
            visitedPath.add(neighbor);
            if (dfs(neighbor, end, gridSize, barriers, visited, parent)) {
                return true;
            }
        }

        return false;
    }

    private List<Point> getNeighbors(Point current, int gridSize, Set<Point> barriers,
                                     Set<Point> visited, boolean allowDiagonal) {
        List<Point> neighbors = new ArrayList<>();
        int[][] directions = allowDiagonal ? DIRECTIONS_8 : DIRECTIONS_4;

        for (int[] direction : directions) {
            int newX = current.getX() + direction[0];
            int newY = current.getY() + direction[1];
            Point neighbor = new Point(newX, newY);

            if (isValid(newX, newY, gridSize) &&
                !barriers.contains(neighbor) &&
                !visited.contains(neighbor)) {
                neighbors.add(neighbor);
            }
        }

        return neighbors;
    }

    private boolean isValid(int x, int y, int gridSize) {
        return x >= 0 && x < gridSize && y >= 0 && y < gridSize;
    }

    private List<Point> reconstructPath(Map<Point, Point> parent, Point start, Point end) {
        List<Point> path = new ArrayList<>();
        Point current = end;

        while (current != null) {
            path.add(0, current);
            current = parent.get(current);
        }

        return path;
    }
}
