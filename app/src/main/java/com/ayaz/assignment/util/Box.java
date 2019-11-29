package com.ayaz.assignment.util;

public class Box {
    int index, x, y, scale;

    public Box(int index, int x, int y, int scale) {
        this.index = index;
        this.x = x;
        this.y = y;
        this.scale = scale;
    }

    public int getIndex() {
        return index;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getScale() {
        return scale;
    }
}
