package com.example.projet_mobile;

public class Point {
    public float x;
    public float y;

    public Point() {
        // Default constructor required for calls to DataSnapshot.getValue(Point.class)
    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }
}