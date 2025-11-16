package org.example.model;

public class Stop {
    private String name;
    private String lineId;
    private String id;
    private double x;
    private double y;

    
    public Stop(String name, String id, double x, double y) {
        this.name = name;
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public Stop(String name, String lineId, String id, double x, double y) {
        this.name = name;
        this.lineId = lineId;
        this.id = id;
        this.x = x;
        this.y = y;
    }
    public Stop(String name) {
        this.name = name;
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
                ", lineId='" + lineId + '\'' +
                ", id='" + id + '\'' +
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
    
}
