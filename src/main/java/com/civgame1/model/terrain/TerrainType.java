package com.civgame1.model.terrain;

public enum TerrainType {
    GRASSLAND(1),
    PLAINS(1),
    FOREST(2),
    MOUNTAIN(4);

    private final int movementCost;

    TerrainType(int movementCost) {
        this.movementCost = movementCost;
    }

    public int getMovementCost() { return movementCost; }
}
