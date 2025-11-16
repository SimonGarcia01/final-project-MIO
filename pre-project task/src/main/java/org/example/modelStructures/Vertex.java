package org.example.modelStructures;

public class Vertex {
    private String name;
    private String lineId;
    private String stopId;
    private double x;
    private double y;
    private int orientation;
    private int variant;
    private int stopSequence;

    // Constructors, overloading of constructor
    public Vertex(String name, String stopId, double x, double y) {
        this.name = name;
        this.stopId = stopId;
        this.x = x;
        this.y = y;
    }

    public Vertex(String name, String id, double x, double y, String lineId, int orientation, int variant, int stopSequence) {
        this.name = name;
        this.lineId = lineId;
        this.stopId = id;
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.variant = variant;
        this.stopSequence = stopSequence;
    }
    public Vertex(String stopId) {
        this.stopId = stopId;
    }

    //@Override
    public String toStringE() {
        return "Stop: {" +
                "name='" + name + '\'' +
                ", id='" + stopId + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}' +
                '\n';
    }

    @Override
    public String toString() {
        return "\nStop: {" +
                "name='" + name + '\'' +
                ", stopId='" + stopId + '\'' +
                ", lineId='" + lineId + '\'' +
                ", orientation='" + orientation + '\'' +
                ", variant='" + variant + '\'' +
                ", stopsequence='" + stopSequence + '\'' +
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
    public String getLineId() {
        return lineId;
    }
    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getVariant() {
        return variant;
    }

    public void setVariant(int variant) {
        this.variant = variant;
    }

    public int getStopSequence() {
        return stopSequence;
    }

    public void setStopSequence(int stopSequence) {
        this.stopSequence = stopSequence;
    }

}
