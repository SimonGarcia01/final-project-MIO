package org.example.model;

public class Stop {
    private String name;
    private String lineId;
    private String id;
    private double x;
    private double y;
    private String orientation;
    private String variant;
    private String stopSequence;

    // PENSAR SI ES MEJOR RELACIONARLO CON LINE line;

    
    public Stop(String name, String id, double x, double y) {
        this.name = name;
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public Stop(String name, String id, double x, double y, String lineId, String orientation, String variant, String stopSequence) {
        this.name = name;
        this.lineId = lineId;
        this.id = id;
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.variant = variant;
        this.stopSequence = stopSequence;
    }
    public Stop(String id) {
        this.id = id;
    }

    //@Override
    public String toStringE() {
        return "Stop: {" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public String toString() {
        return "Stop: {" +
                "name='" + name + '\'' +
                ", stopId='" + id + '\'' +
                ", lineId='" + lineId + '\'' +
                ", orientation='" + orientation + '\'' +
                ", variant='" + variant + '\'' +
                ", stopsequence='" + stopSequence + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
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

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getStopSequence() {
        return stopSequence;
    }

    public void setStopSequence(String stopSequence) {
        this.stopSequence = stopSequence;
    }

}
