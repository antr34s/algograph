package com.algofind.service;

import com.algofind.PathfindingRequest;
import com.algofind.PathfindingRequest.Point;
import com.algofind.PathfindingResponse;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AStarService implements PathfindingService {

    private static final int[][] DIRECTIONS_4 = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    private static final int[][] DIRECTIONS_8 = {
        {0, 1}, {1, 0}, {0, -1}, {-1, 0},
        {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
    };

    private static final double SQRT2 = Math.sqrt(2);
    private static final double WEIGHT = 1.0;

    private static class Node implements Comparable<Node> {
        Point point;
        double gScore;
        double fScore;

        Node(Point point, double gScore, double fScore) {
            this.point = point;
            this.gScore = gScore;
            this.fScore = fScore;
        }

        @Override
        public int compareTo(Node other) {
            return Double.compare(this.fScore, other.fScore);
        }
    }

    @Override
    public PathfindingResponse execute(PathfindingRequest request) {
        int gridSize = request.getGridSize();
        Point start = request.getStart();
        Point end = request.getEnd();
        Set<Point> barriers = new HashSet<>(request.getBarriers() != null ? request.getBarriers() : new ArrayList<>());
        boolean allowDiagonal = request.isAllowDiagonal();

        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Map<Point, Double> gScore = new HashMap<>();
        Map<Point, Point> parent = new HashMap<>();
        Set<Point> closedSet = new HashSet<>();
        List<Point> visitedPath = new ArrayList<>();

        double startH = heuristic(start, end, allowDiagonal);
        openSet.offer(new Node(start, 0, startH));
        gScore.put(start, 0.0);
        parent.put(start, null);

        int nodesExplored = 0;

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            Point currentPoint = current.point;

            if (closedSet.contains(currentPoint)) {
                continue;
            }

            closedSet.add(currentPoint);
            visitedPath.add(currentPoint);
            nodesExplored++;

            if (currentPoint.equals(end)) {
                List<Point> path = reconstructPath(parent, start, end);
                return new PathfindingResponse(path, visitedPath, nodesExplored, 0, true, getAlgorithmName());
            }

            List<Point> neighbors = getNeighbors(currentPoint, gridSize, barriers, closedSet, allowDiagonal);

            for (Point neighbor : neighbors) {
                double movementCost = isDiagonalMove(currentPoint, neighbor) ? SQRT2 : 1.0;
                double tentativeGScore = gScore.get(currentPoint) + movementCost;

                if (!gScore.containsKey(neighbor) || tentativeGScore < gScore.get(neighbor)) {
                    gScore.put(neighbor, tentativeGScore);
                    double h = heuristic(neighbor, end, allowDiagonal);
                    double f = tentativeGScore + WEIGHT * h;
                    parent.put(neighbor, currentPoint);
                    openSet.offer(new Node(neighbor, tentativeGScore, f));
                }
            }
        }

        return new PathfindingResponse(new ArrayList<>(), visitedPath, nodesExplored, 0, false, getAlgorithmName());
    }

    private List<Point> getNeighbors(Point current, int gridSize, Set<Point> barriers,
                                     Set<Point> closedSet, boolean allowDiagonal) {
        List<Point> neighbors = new ArrayList<>();
        int[][] directions = allowDiagonal ? DIRECTIONS_8 : DIRECTIONS_4;

        for (int[] direction : directions) {
            int newX = current.getX() + direction[0];
            int newY = current.getY() + direction[1];
            Point neighbor = new Point(newX, newY);

            if (isValid(newX, newY, gridSize) &&
                !barriers.contains(neighbor) &&
                !closedSet.contains(neighbor)) {
                neighbors.add(neighbor);
            }
        }

        return neighbors;
    }

    private double heuristic(Point a, Point b, boolean allowDiagonal) {
        int dx = Math.abs(a.getX() - b.getX());
        int dy = Math.abs(a.getY() - b.getY());

        if (allowDiagonal) {
            return dx + dy + (SQRT2 - 2) * Math.min(dx, dy);
        } else {
            return dx + dy;
        }
    }

    private boolean isDiagonalMove(Point from, Point to) {
        return from.getX() != to.getX() && from.getY() != to.getY();
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

    @Override
    public String getAlgorithmName() {
        return "A_STAR";
    }
}
