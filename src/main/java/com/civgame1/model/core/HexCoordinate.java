package com.civgame1.model.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public final class HexCoordinate {

    public final int q;
    public final int r;


    private static final int[][] DIRECTIONS = {
            { 1,  0}, { 1, -1}, { 0, -1},
            {-1,  0}, {-1,  1}, { 0,  1}
    };

    public HexCoordinate(int q, int r) {
        this.q = q;
        this.r = r;
    }


    public int s() {
        return -q - r;
    }


    public int distanceTo(HexCoordinate other) {
        return (Math.abs(q - other.q)
                + Math.abs(r - other.r)
                + Math.abs(s() - other.s())) / 2;
    }


    public List<HexCoordinate> neighbors() {
        List<HexCoordinate> result = new ArrayList<>(6);
        for (int[] d : DIRECTIONS) {
            result.add(new HexCoordinate(q + d[0], r + d[1]));
        }
        return result;
    }


    public HexCoordinate neighbor(int direction) {
        if (direction < 0 || direction > 5)
            throw new IllegalArgumentException("Direction must be 0-5");
        int[] d = DIRECTIONS[direction];
        return new HexCoordinate(q + d[0], r + d[1]);
    }


    public double[] toPixel(double hexSize) {
        double x = hexSize * (3.0 / 2.0 * q);
        double y = hexSize * (Math.sqrt(3) / 2.0 * q + Math.sqrt(3) * r);
        return new double[]{x, y};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HexCoordinate other)) return false;
        return q == other.q && r == other.r;
    }

    @Override
    public int hashCode() {
        return Objects.hash(q, r);
    }

    @Override
    public String toString() {
        return "Hex(" + q + ", " + r + ")";
    }
}
