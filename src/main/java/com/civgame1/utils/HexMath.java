package com.civgame1.utils;

import com.civgame1.model.core.HexCoordinate;

import java.util.ArrayList;
import java.util.List;

public class HexMath {

    public static List<HexCoordinate> hexesInRange(HexCoordinate center, int radius) {
        List<HexCoordinate> results = new ArrayList<>();
        for (int dq = -radius; dq <= radius; dq++) {
            for (int dr = Math.max(-radius, -dq - radius); dr <= Math.min(radius, -dq + radius); dr++) {
                results.add(new HexCoordinate(center.q + dq, center.r + dr));
            }
        }
        return results;
    }

    public static int distance(HexCoordinate a, HexCoordinate b) {
        return (Math.abs(a.q - b.q)
                + Math.abs(a.q + a.r - b.q - b.r)
                + Math.abs(a.r - b.r)) / 2;
    }

    public static List<HexCoordinate> hexLine(HexCoordinate a, HexCoordinate b) {
        int N = distance(a, b);
        List<HexCoordinate> results = new ArrayList<>();
        if (N == 0) {
            results.add(a);
            return results;
        }
        for (int i = 0; i <= N; i++) {
            double t = (double) i / N;
            int q = (int) Math.round(a.q + (b.q - a.q) * t);
            int r = (int) Math.round(a.r + (b.r - a.r) * t);
            results.add(new HexCoordinate(q, r));
        }
        return results;
    }
}
