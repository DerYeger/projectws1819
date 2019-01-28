package de.uniks.liverisk.util;

public class Triangle {

    private Point firstPoint;
    private Point secondPoint;
    private Point thirdPoint;

    Triangle(final Point firstPoint, final Point secondPoint, final Point thirdPoint) {
        this.firstPoint = firstPoint;
        this.secondPoint = firstPoint;
        this.thirdPoint = thirdPoint;
    }

    Point getFirstPoint() {
        return firstPoint;
    }

    Point getSecondPoint() {
        return secondPoint;
    }

    Point getThirdPoint() {
        return thirdPoint;
    }

    boolean hasPoint(final Point point) {
        return point.equals(firstPoint)
                || point.equals(secondPoint)
                || point.equals(thirdPoint);
    }
}
