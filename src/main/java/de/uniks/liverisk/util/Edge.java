package de.uniks.liverisk.util;

public class Edge {

    private Point firstPoint;
    private Point secondPoint;

    Edge(final Point firstPoint, final Point secondPoint) {
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
    }

    Point getFirstPoint() {
        return firstPoint;
    }

    Point getSecondPoint() {
        return secondPoint;
    }
}
