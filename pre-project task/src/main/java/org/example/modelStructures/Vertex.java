package org.example.modelStructures;

public class Vertex {
    private String name;
    private String stopId;
    private double x;
    private double y;

    // Constructors, overloading of constructor
    public Vertex(String name, String stopId, double x, double y) {
        this.name = name;
        this.stopId = stopId;
        this.x = x;
        this.y = y;
    }

    public Vertex(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "\nStop: {" +
                "name='" + name + '\'' +
                ", stopId=" + stopId +
                ", x=" + x +
                ", y=" + y +
                '}' +
                '\n';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

}