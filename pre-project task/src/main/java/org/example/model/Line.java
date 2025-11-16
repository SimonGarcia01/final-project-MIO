package org.example.model;

public class Line {
    String name;
    String variant;

    public Line(String name, String variant) {
        this.name = name;
        this.variant = variant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }
    
}
