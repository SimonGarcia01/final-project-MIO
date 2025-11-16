package org.example.structures;


public class Vertex<V> implements Comparable<Vertex<V>> {
    V value;
    double distance = Double.POSITIVE_INFINITY;
    Color color = Color.WHITE;
    Vertex<V> predecessor = null;

    public Vertex(V value) {
        this.value = value;
    }

    @Override
    public int compareTo(Vertex<V> other) {
        return Double.compare(this.distance, other.distance);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Vertex<V> getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(Vertex<V> predecessor) {
        this.predecessor = predecessor;
    }

    
}
