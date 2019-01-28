package de.uniks.liverisk.util;

//http://page.mi.fu-berlin.de/faniry/files/faniry_aims.pdf

import de.uniks.liverisk.model.Platform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class DelaunayTriangulator {

    private Point omega1;
    private Point omega2;
    private Point omega3;

    private ArrayList<Platform> platforms;

    private ArrayList<Triangle> triangles;

    public ArrayList<Triangle> generatePlatformConnections(final Collection<Platform> platforms) {
        this.platforms = new ArrayList<>(platforms);
        triangles = new ArrayList<>();

        initialize();

        Collections.shuffle(this.platforms);

        for (Platform platform : platforms) {
            Point point = new Point(platform.getXPos(), platform.getYPos());
            Triangle containingTriangle = getContainingTriangle(point);

            ArrayList<Triangle> newTriangles;
            if (liesInsideofTriangle( containingTriangle, point)) {
                //divide triangle
                newTriangles = divideTriangle(containingTriangle, point);
            } else { //point lies on edge e of triangle
                Edge edge = getContainingEdge(containingTriangle, point);
                Triangle edgeSharingTriangle = getEdgeSharingTriangle(containingTriangle, edge);
                newTriangles = divideTriangle(containingTriangle, point, edgeSharingTriangle);
            }
            for (Triangle triangle : newTriangles) {
                validEdge(triangle, point);
            }
        }
        removeHelperTriangles();
        return triangles;
    }

    private void initialize() {
        omega1 = new Point(400, -933);
        omega2 = new Point(-600, 800);
        omega3 = new Point(1400, 800);
        Triangle triangle = new Triangle(omega1, omega2, omega3);
        triangles.add(triangle);
    }

    private void removeHelperTriangles() {
        for (Triangle triangle : triangles) {
            if (triangle.hasPoint(omega1)
                    || triangle.hasPoint(omega2)
                    || triangle.hasPoint(omega3)) {
                triangles.remove(triangle);
            }
        }
    }

    //TODO implement
    private Triangle getContainingTriangle(final Point point) {
        return null;
    }

    //TODO implement
    private Edge getContainingEdge(final Triangle triangle, final Point point) {
        return null;
    }

    //TODO implement
    private boolean liesInsideofTriangle(final Triangle triangle, final Point point) {
        return true;
    }

    //TODO implement
    //divides input into 3 new triangles
    private ArrayList<Triangle> divideTriangle(final Triangle triangle, final Point point) {
        return null;
    }

    //TODO implement
    //divides input into 2 new triangles
    private ArrayList<Triangle> divideTriangle(final Triangle triangle, final Point point, final Triangle adjacentTriangle) {
        return null;
    }

    //TODO implement
    //return the triangle sharing an edge from a to b with the input triangle
    private Triangle getEdgeSharingTriangle(final Triangle triangle, final Edge edge) {
        return null;
    }

    private void validEdge(final Triangle triangle, final Point point) {
        Triangle adjacentTriangle = getAdjacentTriangle(triangle, point);
        if (inCircle(adjacentTriangle, point)) {
            //edge flip
            ArrayList<Triangle> newTriangles = flip(triangle, adjacentTriangle, point);
            for (Triangle newTriangle : newTriangles) {
                validEdge(newTriangle, point);
            }
        }
    }

    //TODO implement
    private Triangle getAdjacentTriangle(final Triangle triangle, final Point point) {
        return null;
    }

    private boolean inCircle(final Triangle triangle, final Point point) {
        Point firstPoint = triangle.getFirstPoint();
        Point secondPoint = triangle.getSecondPoint();
        Point thirdPoint = triangle.getThirdPoint();
        double[][] matrix = {{firstPoint.getX(), secondPoint.getY(), Math.pow(firstPoint.getX(), 2) + Math.pow(firstPoint.getY(), 2), 1},
                {secondPoint.getX(), secondPoint.getY(), Math.pow(secondPoint.getX(), 2) + Math.pow(secondPoint.getY(), 2), 1},
                {thirdPoint.getX(), thirdPoint.getY(), Math.pow(thirdPoint.getX(), 2) + Math.pow(thirdPoint.getY(), 2), 1},
                {point.getX(), point.getY(), Math.pow(point.getX(), 2) + Math.pow(point.getY(), 2), 1}};
        return MathHelper.calculateDeterminant(matrix, 4) > 0;
    }

    //TODO implement
    private ArrayList<Triangle> flip(final Triangle a, final Triangle b, final Point point) {
        return null;
    }
}
